const lib = require('./lib');
let data;

// Lambda function index.handler - thin wrapper around lib.authenticate
module.exports.handler = async (even, context, callback) => {
    try {
        data = await lib.authenticate(event);
    }
    catch (err) {
        callback('Unauthorized');
    }
    console.log('Returning data');
    return data;
};
