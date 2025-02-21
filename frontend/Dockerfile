# Etapa 1: Usaremos Node.js como imagen base
FROM node:20
WORKDIR /app

# Copiar archivos de configuración e instalar las dependencias
COPY package*.json ./
RUN npm install

# Copiar todo el código fuente
COPY . .

# Exponer el puerto en el que se ejecutará la aplicación Angular
EXPOSE 4200

# Comando para ejecutar la aplicación con ng serve
CMD ["npx", "ng", "serve", "--host", "0.0.0.0"]
