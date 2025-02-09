FROM node:14

WORKDIR /app

COPY package.json ./

RUN bun install

COPY . .

EXPOSE 4000


CMD ["bun", "start"]