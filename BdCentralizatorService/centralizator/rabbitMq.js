const amqp = require('amqplib');

async function connectToRabbitMQ() {
    try {
        const connection = await amqp.connect('amqp://localhost:5672'); // Update your RabbitMQ server URI if needed
        const channel = await connection.createChannel();
        await channel.assertQueue('centralizator', {
            durable: true
        });
        await channel.assertQueue('file-accepted', {
            durable: true
        });
        console.log('Connected to RabbitMQ and queue ensured');
        return { connection, channel };
    } catch (error) {
        console.error('Failed to connect to RabbitMQ:', error);
    }
}

async function listenForMessages(processMessageCallback) {
    const { channel } = await connectToRabbitMQ(); // Ensure connection to RabbitMQ and get channel
    if (!channel) return;

    const queue = 'centralizator'; // The name of the queue to listen to

    channel.consume(queue, (msg) => {
        if (msg !== null) {
            console.log('Received a message from RabbitMQ:', msg.content.toString());
            const headers = msg.properties.headers;
            const message = msg;
            processMessageCallback(message,headers["collection"]);
            channel.ack(msg); // Acknowledge message as processed
        }
    }, {
        noAck: false // Turn off auto-acknowledgment
    });

    console.log(`Listening for messages on queue "${queue}"...`);
}

async function listenForFiles(processFileCallback){
    const { channel } = await connectToRabbitMQ(); // Ensure connection to RabbitMQ and get channel
    if (!channel) return;

    const queue = 'file-accepted'; // The name of the queue to listen to

    channel.consume(queue, (msg) => {
        if (msg !== null) {
            console.log('Received a file from RabbitMQ:');
            const headers = msg.properties.headers;
            const message = msg;
            processFileCallback(message,headers["collection"], headers["file-name"]);
            channel.ack(msg); // Acknowledge message as processed
        }
    }, {
        noAck: false // Turn off auto-acknowledgment
    });

    console.log(`Listening for messages on queue "${queue}"...`);
}

exports.connectToRabbitMQ = connectToRabbitMQ;
exports.listenForMessages = listenForMessages;
exports.listenForFiles = listenForFiles;