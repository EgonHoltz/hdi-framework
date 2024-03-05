const express = require('express');
const grpc = require('grpc');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const { dataProto, getData } = require('./gRPCController');
const reflection = require('grpc-server-reflection');

const app = express();
app.use(bodyParser.json());

// Connect to MongoDB
mongoose.connect('mongodb://localhost/hdi-database', { useUnifiedTopology: true })
  .then(() => {
    console.log('MongoDB connected');
    stratGrpc()
});

function stratGrpc() {
  const server = new grpc.Server();
  server.addService(dataProto.DataService.service, { GetData: getData });
  server.bind('0.0.0.0:8060', grpc.ServerCredentials.createInsecure());
  console.log('Server running at http://localhost:8060');
  server.start();
}