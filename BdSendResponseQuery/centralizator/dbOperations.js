const mongoose = require('mongoose');
const grpc = require('grpc');

async function findOnCollectionByFields(collectionName, fields, callback) {
  if (collectionName ==  null || collectionName === undefined){
    console.log("Without collectionName: " +collectionName);
    return callback(
      {
        code: grpc.status.INTERNAL,
        message: "Without collectionName",
      }, "Without collectionName" )
  }
  if (fields === "" || fields ==  null || fields === undefined){
    console.log("Without fields: " +fields);
    return callback(
      {
        code: grpc.status.INTERNAL,
        message: "Without fields",
      }, "Without fields" )
  }
  try {
    fieldsJson = JSON.parse(fields);
    let dynamicModel;
    if (mongoose.models[collectionName]) {
      dynamicModel = mongoose.model(collectionName);
    } else {
      const dynamicSchema = new mongoose.Schema({}, { strict: false });
      dynamicModel = mongoose.model(collectionName, dynamicSchema, collectionName);
    }

    const docs = await dynamicModel.find(fieldsJson).exec();

    callback(null, { data: JSON.stringify(docs) });
  } catch (err) {
    console.error('Error:', err);
    callback({
      code: grpc.status.INTERNAL,
      message: err.message,
    });
  }
}

exports.findOnCollectionByFields = findOnCollectionByFields