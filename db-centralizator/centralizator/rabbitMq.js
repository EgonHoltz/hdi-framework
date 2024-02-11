const amqp = require('amqplib');

async function connectToRabbitMQ() {
    try {
        const connection = await amqp.connect('amqp://localhost:5672'); // Update your RabbitMQ server URI if needed
        const channel = await connection.createChannel();
        await channel.assertQueue('dataQueue', {
            durable: false
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

    const queue = 'dataQueue'; // The name of the queue to listen to

    channel.consume(queue, (msg) => {
        if (msg !== null) {
            console.log('Received a message from RabbitMQ:', msg.content.toString());
            const message = JSON.parse(msg.content.toString());
            processMessageCallback(message);
            channel.ack(msg); // Acknowledge message as processed
        }
    }, {
        noAck: false // Turn off auto-acknowledgment
    });

    console.log(`Listening for messages on queue "${queue}"...`);
}

exports.connectToRabbitMQ = connectToRabbitMQ;
exports.listenForMessages = listenForMessages;