const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const { checkDuplicationAndAmend } = require('./dbOperations');
const { updateCache, getCachedCollection } = require('./cacheManager');
const { connectToRabbitMQ, listenForMessages } = require('./rabbitMq');
const PORT = process.env.PORT || 8022;

const app = express();
app.use(bodyParser.json());

// Connect to MongoDB
mongoose.connect('mongodb://localhost/hdi-database', { useUnifiedTopology: true })
  .then(() => {
    console.log('MongoDB connected');
    app.listen(PORT, () => {
      console.log(`Server is running on port ${PORT}`);
      updateCache().then(() => console.log('Cache initialized at startup.'));
    });
})

// RabbitMQ setup and message processing
connectToRabbitMQ().then(() => {
  listenForMessages(processMessage);
});

// Process message from RabbitMQ
async function processMessage(message,collection) {
  try {
    const messageObject = JSON.parse(message.content.toString());
    const collectionName = collection;

    console.log(`Collection Name: ${collectionName}`);
    console.log(`JSON Object:`, messageObject);
    await await checkDuplicationAndAmend(collectionName, messageObject);
  } catch (error) {
      console.error('Error processing message:', error);
  }
}

app.post('/updateCache', async (req, res) => {
  try {

    await updateCache();
    res.send('Cache updated successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
});