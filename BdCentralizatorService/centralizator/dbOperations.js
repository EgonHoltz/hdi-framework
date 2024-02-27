const mongoose = require('mongoose');

async function checkDuplicationAndAmend(collectionName, data, cachedFields) {
  if (collectionName ==  null || collectionName === undefined){
    console.log("the JSON ahead came without header: " +data);
    return;
  }
  const db = mongoose.connection.db;
  const collection = db.collection(collectionName);
  const schemaFields = cachedFields || Object.keys(data);

  // Build query to check for duplications based on cached schema fields
  const query = schemaFields.reduce((acc, field) => {
    if (data.hasOwnProperty(field)) {
      acc[field] = data[field];
    }
    return acc;
  }, {});

  const existingDocument = await collection.findOne(query);

  if (existingDocument) {
    console.log("Similar information found")
    // Check if all fields match (duplicate) or if an amendment is needed
    const isDuplicate = schemaFields.every(field => data[field] === existingDocument[field]);
    if (isDuplicate) {
      console.log("Duplicated document to dump")
      // Send to catched collection and discard incoming data
      await sendToCatchedCollection("dup", collectionName, data);
      return { status: 'Duplicate', message: 'Data is duplicated and has been sent to the catched collection.' };
    } else {
      // Amend data (merge and update)
      console.log("Amending data")
      await sendToCatchedCollection("amd", collectionName, data);
      const updatedData = { ...existingDocument, ...data };
      await collection.updateOne({ _id: existingDocument._id }, { $set: updatedData });
      return { status: 'Amended', message: 'Data amended successfully with additional information.' };
    }
  } else {
    console.log("Adding new data")
    // If no document matches, insert new data
    await collection.insertOne(data);
    console.log("***New data inserted!!***")
    return { status: 'Inserted', message: 'Data inserted successfully.' };
  }
}

async function sendToCatchedCollection(type,collectionName, data) {
  const catchedModel = mongoose.model('catched', new mongoose.Schema({ type: String, collectionName: String, data: Object }, { strict: false }));
  await catchedModel.create({ type, collectionName, data });
}

exports.checkDuplicationAndAmend = checkDuplicationAndAmend