const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const { addToRejection } = require('./dbOperations');
const { addFileToRejection } = require('./minioOperations');
const { connectToRabbitMQ, listenForMessages, listenForFiles } = require('./rabbitMq');
const PORT = process.env.PORT || 8052;

const app = express();
app.use(bodyParser.json());

// Connect to MongoDB
mongoose.connect('mongodb://localhost/hdi-database', { useUnifiedTopology: true })
  .then(() => {
    console.log('MongoDB connected');
    app.listen(PORT, () => {
      console.log(`Server is running on port ${PORT}`);
    });
})

// RabbitMQ setup and message processing
connectToRabbitMQ().then(() => {
  listenForMessages(processMessage);
  listenForFiles(processFile);
});

// Process message from RabbitMQ
async function processMessage(message,collection,reason) {
  try {
    const messageBuffer = message.content.toString('utf-8')
    console.log("Message Buffer: " + messageBuffer);
    const messageObject = JSON.parse(messageBuffer);

    console.log(`Collection Name: ${collection} with reason: ${reason}`);
    console.log(`JSON Object:`, messageObject);
    await addToRejection(collection,reason, messageObject);
  } catch (error) {
      console.error('Error processing message:', error);
  }
}

async function processFile(message,fileName) {
  try {
    const fileContent = message.content;
    const parts = fileName.split('.');
    const extension = parts.pop();
    const baseName = parts.join('.');
    const now = new Date();
    const timestamp = now.toISOString()
        .replace(/:/g, '') 
        .replace(/-/g, '')
        .replace('T', '-') 
        .split('.')[0];
    const newFileName = `${baseName}_${timestamp}.${extension}`;

    console.log(`File Name: ${newFileName}`);
    await addFileToRejection(newFileName, fileContent);
  } catch (error) {
      console.error('Error processing message:', error);
  }
}
