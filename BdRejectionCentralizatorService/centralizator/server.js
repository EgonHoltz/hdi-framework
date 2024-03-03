const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const { addToRejection } = require('./dbOperations');
const { connectToRabbitMQ, listenForMessages } = require('./rabbitMq');
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
