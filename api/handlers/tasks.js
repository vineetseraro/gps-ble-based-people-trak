const commonHelper = require('../helpers/common');
const bluebirdPromise = require('bluebird');
const messages = require('../mappings/messagestring.json');
const mongoose = require('mongoose');
const taskHelper = require('../helpers/tasks');
const akResponse = require('../lib/respones');
const clientHandler = require('../lib/clientHandler');
const currentUserHandler = require('../lib/currentUserHandler');
/**
 * Get task List
 *  
 * @param {Object} event event passed to the lambda
 * @param {Object} context context passed to the lambda
 * @callback callback Lambda Callback
 */
module.exports.getTasks = (event, context, callback) => {
  commonHelper.decryptDbURI().then(dbURI => {
    const parsedEvent = commonHelper.parseLambdaEvent(event);

    clientHandler.setClient(clientHandler.getClientObject(parsedEvent));
    currentUserHandler.setCurrentUser(currentUserHandler.getCurrentUserObject(parsedEvent));

    commonHelper.connectToDb(dbURI);

    bluebirdPromise
      .all([
        taskHelper.get(
          taskHelper.getFilterParams(parsedEvent),
          taskHelper.getExtraParams(parsedEvent)
        ),
        taskHelper.count(taskHelper.getFilterParams(parsedEvent))
      ])
      .then(resultObj => {
        const response = akResponse.listSuccess(resultObj, messages.ATTRIBUTE_LIST);

        mongoose.disconnect();
        callback(null, response);
      })
      .catch(() => {
        // TODO : please remove 404 from catch block as it is a valid success response. catch should only catught exceptions and return with status in range of 500
        const response = akResponse.noDataFound(messages.NO_RECORDS, messages.NO_RECORDS);
        mongoose.disconnect();
        callback(null, response);
      });
  });
};

/**
 * Get Single task for specified ID
 * 
 * @param {Object} event event passed to the lambda
 * @param {Object} context context passed to the lambda
 * @callback callback Lambda Callback
 */
module.exports.getTaskById = (event, context, callback) => {
  // console.log(event);
  commonHelper.decryptDbURI().then(dbURI => {
    const parsedEvent = commonHelper.parseLambdaEvent(event);
    clientHandler.setClient(clientHandler.getClientObject(parsedEvent));
    currentUserHandler.setCurrentUser(currentUserHandler.getCurrentUserObject(parsedEvent));

    commonHelper.connectToDb(dbURI);
    taskHelper.setHeaders(parsedEvent.headers);

    taskHelper
      .getById(parsedEvent.pathParameters.id)
      .then(result => {
        const response = akResponse.success(result, messages.ATTRIBUTE_FETCH_SUCCESS, messages.OK);

        mongoose.disconnect();
        callback(null, response);
      })
      .catch(() => {
        // TODO : please remove 404 from catch block as it is a valid success response. catch should only catught exceptions and return with status in range of 500
        const response = akResponse.noDataFound(messages.NO_RECORDS, messages.NO_RECORDS);
        mongoose.disconnect();
        callback(null, response);
      });
  });
};

/**
 * Update an task.
 * 
 * @param {Object} event event passed to the lambda
 * @param {Object} context context passed to the lambda
 * @callback callback Lambda Callback
 */

module.exports.updateTask = (event, context, callback) => {
  commonHelper.decryptDbURI().then(dbURI => {
    const parsedEvent = commonHelper.parseLambdaEvent(event);
    clientHandler.setClient(clientHandler.getClientObject(parsedEvent));
    currentUserHandler.setCurrentUser(currentUserHandler.getCurrentUserObject(parsedEvent));

    commonHelper.connectToDb(dbURI);
    taskHelper
      .validateUpdate(parsedEvent)
      .then(() => {
        taskHelper
          .update(parsedEvent)
          .then(() => taskHelper.getById(parsedEvent.pathParameters.id, true))
          .then(result => {
            const response = akResponse.success(
              result,
              messages.ATTRIBUTE_UPDATE_SUCCESS,
              messages.OK
            );

            mongoose.disconnect();
            callback(null, response);
          })
          .catch(() => {
            // TODO : please remove 301 from catch block as it is a not valid success response. catch should only catught exceptions and return with status in range of 500
            const response = akResponse.notModified(
              messages.ATTRIBUTE_UPDATE_FAIL,
              messages.ATTRIBUTE_UPDATE_FAIL
            );

            mongoose.disconnect();
            callback(null, response);
          });
      })
      .catch(errors => {
        // TODO : please remove 422 from catch block as it is a not valid success response. catch should only catught exceptions and return with status in range of 500
        const response = akResponse.validationFailed(
          errors,
          messages.VALIDATION_ERRORS_OCCOURED,
          messages.VALIDATION_ERROR
        );

        mongoose.disconnect();
        callback(null, response);
      });
  });
};

/**
 * Save an task.
 * 
 * @param {Object} event event passed to the lambda
 * @param {Object} context context passed to the lambda
 * @callback callback Lambda Callback
 */
module.exports.saveTask = (event, context, callback) => {
  commonHelper.decryptDbURI().then(dbURI => {
    const parsedEvent = commonHelper.parseLambdaEvent(event);
    clientHandler.setClient(clientHandler.getClientObject(parsedEvent));
    currentUserHandler.setCurrentUser(currentUserHandler.getCurrentUserObject(parsedEvent));

    commonHelper.connectToDb(dbURI);

    taskHelper
      .validateRequest(parsedEvent)
      .then(() => {
        taskHelper
          .save(parsedEvent)
          .then(result => taskHelper.getById(result._id, true))
          .then(result => {
            const response = akResponse.created(
              result,
              messages.ATTRIBUTE_SAVE_SUCCESS,
              messages.OK
            );

            mongoose.disconnect();
            callback(null, response);
          })
          .catch(() => {
            // TODO : please remove 400 from catch block as it is a not valid response it will be send if Json format is not valid. catch should only catught exceptions and return with status in range of 500
            const response = akResponse.badRequest(
              messages.ATTRIBUTE_CREATE_FAIL,
              messages.ATTRIBUTE_CREATE_FAIL
            );

            mongoose.disconnect();
            callback(null, response);
          });
      })
      .catch(errors => {
        // TODO : please remove 422 from catch block as it is a not valid success response. catch should only catught exceptions and return with status in range of 500
        const response = akResponse.validationFailed(
          errors,
          messages.VALIDATION_ERRORS_OCCOURED,
          messages.VALIDATION_ERROR
        );
        mongoose.disconnect();
        callback(null, response);
      });
  });
};
