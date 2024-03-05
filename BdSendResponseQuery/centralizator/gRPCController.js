const path = require('path');
const grpc = require('grpc');
const protoLoader = require('@grpc/proto-loader');
const { findOnCollectionByFields } = require('./dbOperations');
const PROTO_PATH = path.resolve(__dirname, '../grpc/data_service.proto');
const packageDefinition = protoLoader.loadSync(PROTO_PATH, {});
const dataProto = grpc.loadPackageDefinition(packageDefinition).data;

function getData(call, callback) {
    console.log("Called getData");
    const collectionName = call.request.collection;
    const fields = call.request.fields;
  
    return findOnCollectionByFields(collectionName, fields, callback);
}

exports.dataProto = dataProto;
exports.getData = getData;
