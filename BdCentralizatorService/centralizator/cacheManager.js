const mongoose = require('mongoose');

// Simulated in-memory cache
let collectionsCache = {};

/**
 * Updates the cache with the latest data from all collections.
 * This is a simplified version that assumes you have a finite list of known collection names.
 * In a real-world scenario, you might dynamically list collections and update them accordingly.
 */
async function updateCache() {
    const db = mongoose.connection.db;
    const collectionNames = await db.listCollections().toArray().then(cols => cols.map(col => col.name));

    for (let name of collectionNames) {
        const collection = db.collection(name);
        const documents = await collection.find({}).toArray();
        collectionsCache[name] = documents;
    }

    console.log('Cache updated successfully');
}

/**
 * Retrieves the cached data for a given collection.
 * @param {string} collectionName - The name of the collection to retrieve from the cache.
 * @returns {Array} An array of documents for the given collection.
 */
function getCachedCollection(collectionName) {
    return collectionsCache[collectionName] || [];
}

exports.updateCache = updateCache;
exports.getCachedCollection = getCachedCollection;