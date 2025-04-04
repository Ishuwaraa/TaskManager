# Fetch node image 
FROM node:20.11.0-alpine AS base

# Set the working directory
WORKDIR /app

# Dependencies stage
FROM base AS deps
COPY package*.json ./
RUN npm install

# Development stage
FROM base AS development
COPY --from=deps /app/node_modules ./node_modules
COPY . .
EXPOSE 4200

# Start the application
CMD ["npx", "@angular/cli", "serve", "--host", "0.0.0.0", "--disable-host-check", "--poll=5000", "--watch"]