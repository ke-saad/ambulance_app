FROM jenkins/inbound-agent:latest
USER root
RUN set -x \
  && retry_apt_update() { \
  local attempt=1; \
  while true; do \
  echo "Attempt ${attempt} to run apt-get update" ; \
  apt-get update -y && break || { \
  if (( attempt >= 3 )); then \
  echo "Max attempts reached. Failing." ; \
  return 1 ; \
  fi ; \
  echo "apt-get update failed. Retrying in ${attempt} seconds" ; \
  sleep "${attempt}" ; \
  attempt=$((attempt+1)) ; \
  } ; \
  done ; \
  return 0 ; \
  }; \
  retry_apt_update && \
  apt-get install -y --no-install-recommends \
  curl \
  git \
  vim \
  ca-certificates \
  gnupg \
  lsb-release \
  sudo


RUN until id -u jenkins; do sleep 1; echo "Waiting for jenkins user..."; done


RUN groupadd docker


RUN curl -fsSL https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz -o docker.tgz && \
  tar -xzf docker.tgz && \
  mv docker/docker /usr/local/bin && \
  rm docker.tgz && \
  rm -rf docker


RUN usermod -aG docker jenkins && \
  usermod -g docker jenkins


RUN if [ -n "$HOST_DOCKER_GID" ]; then \
  groupmod -g "$HOST_DOCKER_GID" docker; \
  else \
  echo "Warning: Could not retrieve HOST_DOCKER_GID, container docker group may have wrong gid."; \
  fi

# RUN chmod 777 /var/run/docker.sock
RUN echo 'jenkins ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers

USER jenkins


WORKDIR /home/jenkins/agent