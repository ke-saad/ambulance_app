# Use a base Nginx image
FROM nginx:alpine

# Copy a simple index.html file
COPY index.html /usr/share/nginx/html/

# Expose port 80
EXPOSE 80

# Command to start Nginx
CMD ["nginx", "-g", "daemon off;"]