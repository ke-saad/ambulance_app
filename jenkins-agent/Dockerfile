# FROM jenkins/inbound-agent:latest

# # Switch to root user for installing packages
# USER root

# # Install dependencies with retry logic
# RUN set -x \
#     && retry_apt_update() { \
#         local attempt=1; \
#         while true; do \
#           echo "Attempt ${attempt} to run apt-get update" ; \
#           apt-get update -y && break || { \
#             if (( attempt >= 3 )); then \
#                echo "Max attempts reached. Failing." ; \
#                return 1 ; \
#             fi ; \
#             echo "apt-get update failed. Retrying in ${attempt} seconds" ; \
#             sleep "${attempt}" ; \
#             attempt=$((attempt+1)) ; \
#           } ; \
#        done ; \
#        return 0 ; \
#      }; \
#      retry_apt_update && \
#     apt-get install -y --no-install-recommends \
#         curl \
#         git \
#         vim \
#         ca-certificates \
#         gnupg \
#         lsb-release \
#         sudo

# # Install Docker
# RUN curl -fsSL https://get.docker.com -o get-docker.sh \
#     && sh get-docker.sh \
#     && rm get-docker.sh

# # Add the Jenkins user to the docker group to allow Docker commands without sudo
# RUN usermod -aG docker jenkins

# # Switch back to the jenkins user
# USER jenkins

# # Set the working directory
# WORKDIR /home/jenkins/agent


# FROM jenkins/inbound-agent:latest

# # Switch to root user for installing packages
# USER root

# # Install dependencies with retry logic
# RUN set -x \
#   && retry_apt_update() { \
#   local attempt=1; \
#   while true; do \
#   echo "Attempt ${attempt} to run apt-get update" ; \
#   apt-get update -y && break || { \
#   if (( attempt >= 3 )); then \
#   echo "Max attempts reached. Failing." ; \
#   return 1 ; \
#   fi ; \
#   echo "apt-get update failed. Retrying in ${attempt} seconds" ; \
#   sleep "${attempt}" ; \
#   attempt=$((attempt+1)) ; \
#   } ; \
#   done ; \
#   return 0 ; \
#   }; \
#   retry_apt_update && \
#   apt-get install -y --no-install-recommends \
#   curl \
#   git \
#   vim \
#   ca-certificates \
#   gnupg \
#   lsb-release \
#   sudo

# RUN until id -u jenkins; do sleep 1; echo "Waiting for jenkins user..."; done

# # Add the Jenkins user to the docker group and make it the primary group
# RUN usermod -aG docker jenkins && \
#   usermod -g docker jenkins

# # Switch back to the jenkins user
# USER jenkins

# # Set the working directory
# WORKDIR /home/jenkins/agent

# FROM jenkins/inbound-agent:latest

# # Switch to root user for installing packages
# USER root

# # Install dependencies with retry logic
# RUN set -x \
#   && retry_apt_update() { \
#   local attempt=1; \
#   while true; do \
#   echo "Attempt ${attempt} to run apt-get update" ; \
#   apt-get update -y && break || { \
#   if (( attempt >= 3 )); then \
#   echo "Max attempts reached. Failing." ; \
#   return 1 ; \
#   fi ; \
#   echo "apt-get update failed. Retrying in ${attempt} seconds" ; \
#   sleep "${attempt}" ; \
#   attempt=$((attempt+1)) ; \
#   } ; \
#   done ; \
#   return 0 ; \
#   }; \
#   retry_apt_update && \
#   apt-get install -y --no-install-recommends \
#   curl \
#   git \
#   vim \
#   ca-certificates \
#   gnupg \
#   lsb-release \
#   sudo

# # Wait for jenkins user
# RUN until id -u jenkins; do sleep 1; echo "Waiting for jenkins user..."; done

# # Create the docker group
# RUN groupadd docker

# # Add the Jenkins user to the docker group and make it the primary group
# RUN usermod -aG docker jenkins && \
#   usermod -g docker jenkins

# # Switch back to the jenkins user
# USER jenkins

# # Set the working directory
# WORKDIR /home/jenkins/agent


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

# Wait for jenkins user
RUN until id -u jenkins; do sleep 1; echo "Waiting for jenkins user..."; done

# Create the docker group
RUN groupadd docker

# Install Docker CLI tools
RUN curl -fsSL https://download.docker.com/linux/static/stable/x86_64/docker-24.0.7.tgz -o docker.tgz && \
  tar -xzf docker.tgz && \
  mv docker/docker /usr/local/bin && \
  rm docker.tgz && \
  rm -rf docker

# Add the Jenkins user to the docker group and make it the primary group
RUN usermod -aG docker jenkins && \
  usermod -g docker jenkins

# Synchronize container docker gid with host docker gid
RUN if [ -n "$HOST_DOCKER_GID" ]; then \
  groupmod -g "$HOST_DOCKER_GID" docker; \
  else \
  echo "Warning: Could not retrieve HOST_DOCKER_GID, container docker group may have wrong gid."; \
  fi

RUN chmod 777 /var/run/docker.sock


# Switch back to the jenkins user
USER jenkins

# Set the working directory
WORKDIR /home/jenkins/agent