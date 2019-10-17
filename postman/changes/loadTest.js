// A sample script to demonstrate parallel collection runs using async from:
// https://github.com/postmanlabs/newman/blob/develop/examples/parallel-collection-runs.js

var path = require('path'), 
    async = require('async'), 
    newman = require('newman'),
    // options for the parallel collection runs
    options = {
        collection: path.join(__dirname, './ExampleEchoRequestMethodsCollection.json')
    },
    // callback function that marks the end of the current collection run, when called
    parallelCollectionRun = function (done) {
        newman.run(options, done);
    };
   
// Run the Postman sample collection thrice, in parallel.
// async.parallel([
//     parallelCollectionRun
// ],
// // error or null that demonstrates whether or not parallel collection run succeeded
// // results as array of collection run summary objects
// function (err, results) {
//     err && console.error(err);

//     results.forEach(function (result) {
//         var failures = result.run.failures;

//         console.info(failures.length ? JSON.stringify(failures.failures, null, 2) :
//             `${result.collection.name} ran successfully.`);
//     });
// });

collections = [parallelCollectionRun, parallelCollectionRun],

// 
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
        err && console.error(err);
    } else {
        collections.push(parallelCollectionRun);
        console.info("Launching test for " + collections.length + " concurrent collections.");
        async.parallel(collections, recursiveCallBack);
    }
};

console.log("Start concurrency tests.");
console.info("Launching test for " + collections.length + " concurrent collection.");
async.parallel(collections, recursiveCallBack);