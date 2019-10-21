// index file for Lambda
// let filePathName = './ExampleEchoRequestMethodsCollection.json';
// let CollectionFileName = process.env.CollectionFileName;
// let EnvironmentFileName = process.env.EnvironmentFileName;

// require("dotenv").config();

let filePathName = process.env.CollectionFileName;
let envFilePathName = process.env.EnvironmentFileName;
console.log('Loading function from ' + filePathName);
console.log('Loading environment variables from ' + envFilePathName);

exports.handler = (event, context, callback) => {

    // A sample script to demonstrate parallel collection runs using async from:
    // https://github.com/postmanlabs/newman/blob/develop/examples/parallel-collection-runs.js

    const path = require('path'), 
        async = require('async'), 
        newman = require('newman'),
        // options for the parallel collection runs
        options = {
            collection: path.join(__dirname, filePathName),
            environment: path.join(__dirname, envFilePathName),
            reporters: ['cli']
        },
        // callback function that marks the end of the current collection run, when called
        parallelCollectionRun = function (done) {
            newman.run(options, done);
        };
    
    // Run the Postman sample collection thrice, in parallel.
    collections = [parallelCollectionRun],
    recursiveCallBack = function (err, results) {
        console.info("Done test for " + collections.length + " concurrent collections.");
        err && console.error(err);
        var fail = false;
        if (results) {
            
            results.forEach(function (result) {
                var failures = result.run.failures;
                if (failures.length) {
                    fail = true;
                }
                console.info(failures.length ? `${result.collection.name} failed.` :
                            `${result.collection.name} ran successfully.`);
            });
        } 
        //keep increasing the number of concurrent calls until something fails
        if (fail || err) {
            console.error("Error when attempting " + collections.length + " calls.");
            console.error(err);
            callback('Newman test run encountered an error.', null)
        
        
        } else {
            // collections.push(parallelCollectionRun);
            // console.info("Launching test for " + collections.length + " concurrent collections.");
            // async.parallel(collections, recursiveCallBack);
            // console.info("completed");
            // console.info(results.run.stats);
            callback(null, 'Test run passed!');
            // callback("Newman test run completed successfully")
        }
    };

    // async.parallel(collections, recursiveCallBack);
    // async.parallel([
    //     parallelCollectionRun,
    //     parallelCollectionRun,
    //     parallelCollectionRun
    // ],
    // // error or null that demonstrates whether or not parallel collection run succeeded
    // // results as array of collection run summary objects
    // function (err, results) {
    //     err && console.error(err);
        
    //     results.forEach(function (result) {
    //         let failures = result.run.failures;

    //         console.log(failures.length ? JSON.stringify(failures.failures, null, 2) :
    //             `${result.collection.name} ran successfully.`);
    //         // return feedback about the collection run

    //     });
    //     console.log("hey");

    // });
    console.log("Start concurrency tests.");
    console.info("Launching test for " + collections.length + " concurrent collection.");
    // return async.parallel;
     async.parallel(collections, recursiveCallBack);

// };

};