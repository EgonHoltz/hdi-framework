const Minio = require('minio');

// MinIO client configuration
const minioClient = new Minio.Client({
    endPoint: 'localhost',
    port: 9000,
    useSSL: false,
    accessKey: 'KMVCyIfvTXqe44syMWfY',
    secretKey: '25cMSs4bWuA29wpPW9k3M9pGqRVbfbqCz3OOPgyz'
});

async function addFileToRejection (fileName, messageObject){
    const bucketName = 'rejections';
    minioClient.putObject(bucketName, fileName, messageObject, function(err, etag) {
        if (err) return console.log(err);
        console.log('File uploaded successfully.');
    });
}

exports.addFileToRejection = addFileToRejection;