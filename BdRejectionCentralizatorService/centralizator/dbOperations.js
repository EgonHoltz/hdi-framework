const mongoose = require('mongoose');

async function addToRejection(collectionName, reason, data) {
  if (collectionName ==  null || collectionName === undefined){
    console.log("the JSON ahead came without header: " +data);
    return;
  }
  const db = mongoose.connection.db;
  const collection = db.collection("rejection");
  const rejectData = {
    "sourceCollection": collectionName,
    "reason": reason,
    "data": data
  }

  console.log("Adding new data")
    await collection.insertOne(rejectData);
    console.log("***New data inserted!!***")
    return { status: 'Inserted', message: 'Data inserted successfully.' };
}

exports.addToRejection = addToRejection