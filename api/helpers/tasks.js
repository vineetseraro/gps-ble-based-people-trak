const taskmodel = require('../models/tasks');
const tagHelper = require('../helpers/tags');
const validator = require('../lib/validatorAsync');
const akUtils = require('../lib/utility');
const jsTypeChecker = require('javascript-type-checker');
const mongoose = require('mongoose');
const bluebirdPromise = require('bluebird');
const commonHelper = require('./common');
const currentUserHandler = require('../lib/currentUserHandler');
const clientHandler = require('../lib/clientHandler');
const moment = require('moment');

const taskDependent = {
  things: 'tasks.id',
  'product.1': 'trackingDetails.currentLocation.address.id',
  'product.2': 'tasks.id',
  kollection: 'items.id',
  location: 'tasks.id'
};

// const search = require('../services/search');

const taskService = function() {};

/**
 * Set client of the helper for use across all functions in the helper
 * 
 * @param {Object} clientObj
 * @return {Void}
 * 
 */
taskService.prototype.setClient = function(clientObj) {
  this.client = clientObj;
};

/**
 * Set headers of the helper for use across all functions in the helper
 * 
 * @param {Object} clientObj
 * @return {Void}
 * 
 */
taskService.prototype.setHeaders = function(headers) {
  this.headers = headers;
};

/**
 * Set headers of the helper for use across all functions in the helper
 * 
 * @param {Object} clientObj
 * @return {Void}
 * 
 */
taskService.prototype.setConfigs = function() {
  // console.log('config');
  return require('./configuration')
    .getConfigurations()
    .then(configs => {
      this.configs = configs;
    });
};
/**
 * Query the database to fetch tasks on the basis of search parameters and other parameters
 * 
 * @param {Object} searchParams search filters
 * @param {Object} otherParams pagination, sorting etc other params.
 * @return {Promise} Promise to represent the result of get operation.
 * 
 */
taskService.prototype.get = function(searchParams, otherParams) {
  return taskmodel
    .find(searchParams)
    .sort(otherParams.sort)
    .skip(otherParams.pageParams.offset)
    .limit(otherParams.pageParams.limit)
    .collation({
      locale: 'en_US',
      caseLevel: false
    })
    .exec()
    .then(result => {
      const list = [];
      if (result) {
        for (let i = 0; i < result.length; i++) {
          list.push(this.formatResponse(result[i], otherParams.isDropdown));
        }
      }
      return list;
    })
    .then(result => {
      if (result.length === 0) {
        return bluebirdPromise.reject();
      }
      return bluebirdPromise.resolve(result);
    });
};

/**
 * Fetch a particular task by providing its ID
 * 
 * @param {String} taskId ID of the task to Fetch
 * @return {Promise} Promise to represent the result of get operation.
 * 
 */
taskService.prototype.getById = function(taskId = 'default') {
  if (!mongoose.Types.ObjectId.isValid(taskId)) {
    return bluebirdPromise.reject();
  }
  // if (!forSearch) {
  //   return search.searchById('tasks', taskId + '');
  // } else {
  let conditions = {
    _id: mongoose.Types.ObjectId(taskId)
  };
  conditions = clientHandler.addClientFilterToConditions(conditions);

  return taskmodel
    .find(conditions)
    .exec()
    .then(result => {
      if (result.length > 0) {
        return bluebirdPromise.resolve(this.formatResponse(result[0]));
      }
      return bluebirdPromise.reject();
    });
  // }
};

/**
 * Fetch a particular task by providing its Code
 * 
 * @param {String} code Code of the task to Fetch
 * @return {Promise} Promise to represent the result of get operation.
 * 
 */
taskService.prototype.getByCode = function(code = '') {
  let conditions = {
    code
  };
  conditions = clientHandler.addClientFilterToConditions(conditions);

  return taskmodel
    .find(conditions)
    .exec()
    .then(result => {
      if (result.length > 0) {
        return bluebirdPromise.resolve(this.formatResponse(result[0]));
      }
      return bluebirdPromise.reject();
    });
};

/**
 * Count tasks on the basis of some search conditions.
 * 
 * @param {Object} searchParams Search Conditions
 * @return {Promise<Number>} Number of matching tasks.
 * 
 */
taskService.prototype.count = function(searchParams = {}) {
  return taskmodel.count(searchParams).exec();
};

/**
 * Performs response from DB operations to return as API response.
 * 
 * @param {Object} data Database operation result.
 * @param {Boolean} isDropdown Whether the APi is requesting a dropdown.
 * @return {Promise} formattedResponse Formatted Response
 * 
 */
taskService.prototype.formatResponse = function(data, isDropdown = false) {
  const formattedResponse = {};
  const tz = (currentUserHandler.getCurrentUser() || {}).timezone;
  if (!isDropdown) {
    formattedResponse.id = data._id;
    formattedResponse.code = data.code;
    formattedResponse.name = data.name;
    formattedResponse.status = data.status;
    // formattedResponse.sysDefined = data.sysDefined;

    formattedResponse.description = data.description;
    formattedResponse.from = data.from;
    formattedResponse.to = data.to;
    formattedResponse.images = data.images || [];
    formattedResponse.notes = data.notes;
    formattedResponse.attendees = data.attendees;
    formattedResponse.location = JSON.parse(JSON.stringify(data.location));
    if ((formattedResponse.location || {}).pointCoordinates) {
      formattedResponse.location.coordinates = {};
      formattedResponse.location.coordinates.latitude =
        formattedResponse.location.pointCoordinates.coordinates[1];
      formattedResponse.location.coordinates.longitude =
        formattedResponse.location.pointCoordinates.coordinates[0];

      formattedResponse.location.pointCoordinates = undefined;
    }

    formattedResponse.updatedOn = data.updatedOn;
    formattedResponse.updatedBy = `${((data || {}).updatedBy || '').firstName} ${((data || {})
      .updatedBy || ''
    ).lastName}`;
    formattedResponse.client = data.client;
    formattedResponse.tags = data.tags;
    // commonHelper.formatDate(data.updatedOn, this.headers, 'dt')
    // .then((date) => {
    //   formattedResponse.updatedOn = date;
    return formattedResponse;
    // });
  }
  formattedResponse.id = data._id;
  formattedResponse.name = data.name;
  return formattedResponse;
};

/**
 * Map keys for sorting.
 * 
 * @param {String} key
 * @return {String} 
 * 
 */
taskService.prototype.getColumnMap = function getColumnMap(key) {
  const map = {
    id: '_id',
    code: 'code',
    name: 'name',
    sysDefined: 'sysDefined',
    updatedOn: 'updatedOn',
    updatedBy: 'updatedBy',
    from: 'from',
    to: 'to'
  };

  if (key) {
    return map[key] || key;
  }
  return map;
};

/**
 * Generate the search conditions for the GET operation.
 * 
 * @param {Object} event Lambda Event
 * @return {Object} filters.
 * 
 */
taskService.prototype.getFilterParams = function(event) {
  let filters = {};

  filters = clientHandler.addClientFilterToConditions(filters);

  if (event.queryStringParameters && event.queryStringParameters.filter) {
    // filters._all = event.queryStringParameters.filter;
    filters.$or = [
      {
        name: new RegExp(event.queryStringParameters.filter, 'i')
      },
      {
        code: new RegExp(event.queryStringParameters.filter, 'i')
      },
      {
        'updatedBy.firstName': new RegExp(event.queryStringParameters.filter, 'i')
      },
      {
        'updatedBy.lastName': new RegExp(event.queryStringParameters.filter, 'i')
      }
    ];
  }

  if (event.queryStringParameters.id) {
    filters._id = mongoose.Types.ObjectId(event.queryStringParameters.id);
  }

  if (event.queryStringParameters.code) {
    filters.code = new RegExp(event.queryStringParameters.code, 'i');
  }

  if (event.queryStringParameters.name) {
    filters.name = new RegExp(event.queryStringParameters.name, 'i');
  }

  if (event.queryStringParameters.status) {
    filters.status = parseInt(event.queryStringParameters.status, 10);
  }
  if (event.queryStringParameters.attendee) {
    filters['attendees.uuid'] = event.queryStringParameters.attendee;
  }

  if (event.queryStringParameters.updatedOnFrom || event.queryStringParameters.updatedOnTo) {
    filters.updatedOn = {};
  }

  if (event.queryStringParameters.updatedOnFrom) {
    filters.updatedOn.$gte = new Date(event.queryStringParameters.updatedOnFrom);
  }

  if (event.queryStringParameters.updatedOnTo) {
    filters.updatedOn.$lte = akUtils.formatToDateFilter(
      new Date(event.queryStringParameters.updatedOnTo)
    );
  }

  if (event.queryStringParameters.startFrom || event.queryStringParameters.startTo) {
    filters.from = {};
  }

  if (event.queryStringParameters.startFrom) {
    filters.from.$gte = new Date(event.queryStringParameters.startFrom);
  }

  if (event.queryStringParameters.startTo) {
    filters.from.$lte = akUtils.formatToDateFilter(new Date(event.queryStringParameters.startTo));
  }

  if (event.queryStringParameters.endFrom || event.queryStringParameters.endTo) {
    filters.to = {};
  }

  if (event.queryStringParameters.endFrom) {
    filters.to.$gte = new Date(event.queryStringParameters.endFrom);
  }

  if (event.queryStringParameters.endTo) {
    filters.to.$lte = akUtils.formatToDateFilter(new Date(event.queryStringParameters.endTo));
  }

  // if (request.queryStringParameters.status === '1' || request.queryStringParameters.status === '0') filters.status = request.queryStringParameters.status === '1';
  if (event.queryStringParameters.dd === '1') {
    filters.status = 1;
    filters.sysDefined = 0;
  }

  if (
    currentUserHandler.getCurrentUser().preferredRole !==
    akUtils.getRoleFromGroupName(process.env.adminGroupName)
  ) {
    filters['attendees.uuid'] = currentUserHandler.getCurrentUser().uuid;
  }

  // if (event.queryStringParameters.date) {
  //   filters.$and = [
  //     {
  //       from: fromToFiltersObj.from
  //     },
  //     {
  //       to: fromToFiltersObj.to
  //     }
  //   ];
  // } else {
  //   filters = Object.assign({}, filters, fromToFiltersObj);
  // }

  // console.log(JSON.stringify(filters));
  // console.log(event.queryStringParameters);

  return filters;
};

/**
 * Generate the extra parameters for the GET operation like sorting, pagination etc.
 * 
 * @param {Object} request Lambda Event
 * @return {Object} extraParams
 * 
 */
taskService.prototype.getExtraParams = function(event) {
  const params = {};
  params.sort = {};
  if (!event.queryStringParameters) {
    params.pageParams = {
      offset: 0,
      limit: 20
    };
    params.sort.updatedOn = -1;
    return params;
  }
  const dd = event.queryStringParameters.dd === '1';
  const offset = event.queryStringParameters.offset ? event.queryStringParameters.offset : 0;
  const limit = event.queryStringParameters.limit ? event.queryStringParameters.limit : 20;
  params.isDropdown = dd;
  params.pageParams = {
    offset: dd ? 0 : parseInt(offset, 10),
    limit: dd ? 65535 : parseInt(limit, 10)
  };
  if (event.queryStringParameters.sort) {
    const sortQuery = event.queryStringParameters.sort;
    const sortColumns = sortQuery.split(',');
    sortColumns.forEach(function(col) {
      let sortOrder = 1;
      col = col.trim();
      const isValidColumn = this.getColumnMap(col) || this.getColumnMap(col.replace('-', ''));
      if (isValidColumn) {
        if (col.startsWith('-')) {
          sortOrder = -1;
          col = col.replace('-', '');
        }

        col = this.getColumnMap(col);
        params.sort[col] = sortOrder;
      }
    }, this);
  } else {
    params.sort.updatedOn = -1;
  }

  return params;
};

/**
 * Performs validations common in both update and save.
 * 
 * @param {Object} event Lambda Event
 * @return {Promise} Resolved promise with populated event if successful. Rejected promise with validation errors otherwise.
 * 
 */
taskService.prototype.commonValidations = function(event) {
  return bluebirdPromise
    .all([
      bluebirdPromise.all([
        validator.required(event.body.code),
        validator.stringLength(0, validator.CODE_MAX_LENGTH, event.body.code),
        validator.notDuplicate('tasks', 'code', event.body.code, event.pathParameters.id)
      ]),
      bluebirdPromise.all([
        validator.required(event.body.name),
        validator.stringLength(0, validator.NAME_MAX_LENGTH, event.body.name)
      ]),
      bluebirdPromise.all([validator.valueAllowed([0, 1], event.body.status)]),
      bluebirdPromise.all([validator.required(event.body.description)]),
      bluebirdPromise.all([validator.required(event.body.from)]),
      bluebirdPromise.all([validator.required(event.body.to)]),
      bluebirdPromise.all([validator.required(event.body.location)]),
      bluebirdPromise.all([
        validator.type('array', event.body.tags),
        validator.duplicateArrayElements(null, event.body.tags),
        validator.validatePopulatableLists('tags', event.body.tags),
        validator.arrayOfType('string', event.body.tags)
      ])
    ])
    .then(result => {
      const validatorErrorsMap = {
        code: {
          index: 0,
          fieldName: 'Code'
        },
        name: {
          index: 1,
          fieldName: 'Name'
        },
        status: {
          index: 2,
          fieldName: 'Status'
        },
        description: {
          index: 3,
          fieldName: 'Description'
        },
        from: {
          index: 4,
          fieldName: 'Event From Time'
        },
        to: {
          index: 5,
          fieldName: 'Event To Time'
        },
        location: {
          index: 6,
          fieldName: 'Location'
        },
        tags: {
          index: 7,
          fieldName: 'Tags'
        }
      };
      const errors = akUtils.mapAndGetValidationErrors(result, validatorErrorsMap);
      if (errors) {
        return bluebirdPromise.reject(errors);
      }
      return bluebirdPromise.resolve();
    });
};

/**
 * Performs update-specific validations before performing common validations.
 * 
 * @param {Object} event Lambda Event
 * @return {Promise} Resolved promise with populated event if successful. Rejected promise with validation errors otherwise.
 * 
 */
taskService.prototype.validateUpdate = function(event) {
  let basicErrors;
  if ((event.queryStringParameters.mobile || 0) === '1') {
    return bluebirdPromise.resolve(event);
  }
  return this.commonValidations(event)
    .catch(errors => errors)
    .then(errorsIfAny => {
      basicErrors = errorsIfAny || {};
      return bluebirdPromise.all([
        bluebirdPromise.all([
          validator.elementExists('tasks', event.pathParameters.id),
          validator.deactivationCheck('tasks', event.body.status, event.pathParameters.id)
        ]),
        bluebirdPromise.all([
          validator.checkSame('tasks', 'code', event.body.code, event.pathParameters.id)
        ])
      ]);
    })
    .then(result => {
      const mapping = {
        global: {
          index: 0,
          fieldName: 'Task'
        },
        code: {
          index: 1,
          fieldName: 'Code'
        }
      };
      const validationResult = akUtils.mapAndGetValidationErrors(result, mapping);
      const combinedErrors = Object.assign({}, basicErrors, validationResult);
      if (!jsTypeChecker.isEmptyObject(combinedErrors)) {
        return bluebirdPromise.reject(
          akUtils.getObjectValues(akUtils.sortErrorsObject(combinedErrors, 'tasks'))
        );
      }
      return bluebirdPromise.resolve();
    })
    .catch(errors => bluebirdPromise.reject(errors));
};

/**
 * Checks if a give code already exists in the database.
 * 
 * @param {String} code Code to check
 * @param {String} excludedObjId Object ID of current object if checking while update.
 * @return {Promise} Resolved promise if duplicate code. Rejected promise otherwise
 * 
 */
taskService.prototype.isDuplicateCode = function(code = '', excludedObjId = null) {
  let conditions;
  if (excludedObjId !== null && mongoose.Types.ObjectId.isValid(excludedObjId)) {
    conditions = {
      code,
      _id: {
        $ne: mongoose.Types.ObjectId(excludedObjId)
      }
    };
  } else {
    conditions = {
      code
    };
  }
  conditions = clientHandler.addClientFilterToConditions(conditions);

  return taskmodel
    .findOne(conditions)
    .exec()
    .then(result => {
      if (result) {
        return bluebirdPromise.resolve();
      }
      return bluebirdPromise.reject();
    });
};

taskService.prototype.isDuplicate = function(field, value = '', excludedObjId = null) {
  let conditions = {};
  conditions[field] = value;
  if (excludedObjId !== null && mongoose.Types.ObjectId.isValid(excludedObjId)) {
    conditions._id = {
      $ne: mongoose.Types.ObjectId(excludedObjId)
    };
  }
  conditions = clientHandler.addClientFilterToConditions(conditions);

  return taskmodel
    .findOne(conditions)
    .exec()
    .then(result => {
      if (result) {
        return bluebirdPromise.resolve();
      }
      return bluebirdPromise.reject();
    });
};
/**
 * Performs save-specific validations before performing common validations.
 * 
 * @param {Object} event Lambda Event
 * @return {Promise} Resolved promise with populated event if successful. Rejected promise with validation errors otherwise.
 * 
 */
taskService.prototype.validateRequest = function(event) {
  return this.commonValidations(event)
    .then(() => {})
    .catch(errors => {
      if (!jsTypeChecker.isEmptyObject(errors)) {
        return bluebirdPromise.reject(
          akUtils.getObjectValues(akUtils.sortErrorsObject(errors, 'tasks'))
        );
      }
      return bluebirdPromise.resolve();
    });
};

/**
 * Save an task
 * 
 * @param {Object} event Lambda Event
 * @return {Promise} Promise to represent the result of save operation.
 * 
 */
taskService.prototype.save = function save(event) {
  return bluebirdPromise
    .all([
      tagHelper.getForPopulation(event.body.tags),
      commonHelper.populateSingleLocation(event.body.location, false, false),
      commonHelper.populateSingleFloor(event.body.floor),
      commonHelper.populateSingleZone(event.body.zone)
    ])
    .then(populatedData => {
      const taskObj = new taskmodel(); // create a new instance of the  model

      if (event.body.location) {
        taskObj.location = populatedData[1] || {};
        taskObj.location.address = populatedData[1].attributes || [];
      }
      if (event.body.floor) {
        taskObj.location.floor = populatedData[2];
      }
      if (event.body.zone) {
        taskObj.location.floor.zone = populatedData[3];
      }

      taskObj.code = event.body.code;
      taskObj.name = event.body.name;
      taskObj.sysDefined = 0;
      taskObj.status = event.body.status;

      taskObj.description = event.body.description;
      taskObj.from = event.body.from;
      taskObj.to = event.body.to;
      taskObj.images = event.body.images || [];
      taskObj.notes = event.body.notes;
      taskObj.attendees = event.body.attendees;

      taskObj.updatedOn = Date.now();
      taskObj.updatedBy = currentUserHandler.getCurrentUser();
      taskObj.client = clientHandler.getClient();
      taskObj.tags = populatedData[0];

      return taskObj.save();
    });
};

/**
 * Update an task
 * 
 * @param {Object} event Lambda Event
 * @return {Promise} Promise to represent the result of update operation.
 * 
 */
taskService.prototype.update = function(event) {
  if ((event.queryStringParameters.mobile || 0) === '1') {
    const conditions = {
      _id: event.pathParameters.id
    };
    const taskUpdateObj = {};

    taskUpdateObj.images = event.body.images;
    taskUpdateObj.notes = event.body.notes;
    const updateParams = {
      $set: taskUpdateObj,
      $inc: {
        __v: 1
      }
    };
    return taskmodel
      .findOneAndUpdate(conditions, updateParams, {
        upsert: false,
        new: true
      })
      .exec()
      .catch(err => {
        akUtils.log(err);
      });
  }
  return bluebirdPromise
    .all([
      tagHelper.getForPopulation(event.body.tags),
      commonHelper.populateSingleLocation(event.body.location, false, false),
      commonHelper.populateSingleFloor(event.body.floor),
      commonHelper.populateSingleZone(event.body.zone)
    ])
    .then(populatedData => {
      let conditions = {
        _id: event.pathParameters.id
      };
      conditions = clientHandler.addClientFilterToConditions(conditions);
      const taskUpdateObj = {};

      if (event.body.location) {
        taskUpdateObj.location = populatedData[1] || {};
        taskUpdateObj.location.address = populatedData[1].attributes || [];
      }
      if (event.body.floor) {
        taskUpdateObj.location.floor = populatedData[2];
      }
      if (event.body.zone) {
        taskUpdateObj.location.floor.zone = populatedData[3];
      }

      taskUpdateObj.code = event.body.code;
      taskUpdateObj.name = event.body.name;
      taskUpdateObj.status = event.body.status;

      taskUpdateObj.description = event.body.description;
      taskUpdateObj.from = event.body.from;
      taskUpdateObj.to = event.body.to;
      taskUpdateObj.images = event.body.images;
      taskUpdateObj.notes = event.body.notes;
      taskUpdateObj.attendees = event.body.attendees;

      taskUpdateObj.client = clientHandler.getClient();
      taskUpdateObj.tags = populatedData[0];
      taskUpdateObj.updatedOn = Date.now();
      taskUpdateObj.updatedBy = currentUserHandler.getCurrentUser();
      taskUpdateObj.sysDefined = 0; // event.body.sysDefined;

      const updateParams = {
        $set: taskUpdateObj,
        $inc: {
          __v: 1
        }
      };

      return taskmodel
        .findOneAndUpdate(conditions, updateParams, {
          upsert: false,
          new: true
        })
        .exec()
        .catch(err => {
          akUtils.log(err);
        });
    });
};

taskService.prototype.getForPopulation = function(idValuePair, allowSysDefined = false) {
  idValuePair = idValuePair || [];
  const idValueMap = {};
  for (let i = 0; i < idValuePair.length; i++) {
    idValueMap[idValuePair[i].id] = idValuePair[i].value;
  }
  const idList = idValuePair.map(pair => mongoose.Types.ObjectId(pair.id));
  let conditions = {
    _id: {
      $in: idList
    },
    status: 1,
    sysDefined: 0
  };
  if (allowSysDefined) {
    conditions.sysDefined = 1;
  }
  conditions = clientHandler.addClientFilterToConditions(conditions);
  return taskmodel
    .find(conditions)
    .exec()
    .then(result =>
      result.sort((a, b) => idList.indexOf(a._id) - idList.indexOf(b._id)).map(result => ({
        id: result._id,
        name: result.name,
        status: result.status,
        sysDefined: result.sysDefined,
        value: idValueMap[String(result._id)] || ''
      }))
    );
};

taskService.prototype.validatePopulatable = function(idList) {
  const isPopulatable = idList.reduce(
    (isValid, id) => isValid && mongoose.Types.ObjectId.isValid(id),
    true
  );
  if (!isPopulatable) {
    return bluebirdPromise.resolve(false);
  }
  idList = idList.map(id => mongoose.Types.ObjectId(id));

  let conditions = {
    _id: {
      $in: idList
    },
    status: 1,
    sysDefined: 0
  };
  conditions = clientHandler.addClientFilterToConditions(conditions);
  return taskmodel
    .count(conditions)
    .exec()
    .then(count => {
      if (count === idList.length) {
        return bluebirdPromise.resolve(true);
      }
      return bluebirdPromise.resolve(false);
    });
};

taskService.prototype.validateInactive = function(taskid = '') {
  const modelpath = '../models/';

  const keys = Object.keys(taskDependent);
  // console.log(keys);
  return bluebirdPromise
    .map(keys, key => {
      // console.log(`in 1${key}`);
      const model = require(modelpath + key.split('.')[0]);
      const condition = taskDependent[key];
      const dict = {};
      dict[condition] = taskid;
      // console.log(JSON.stringify(dict));
      return model.findOne(dict).exec();
    })
    .then(result => {
      // console.log(result.length);
      for (let i = 0; i < result.length; i++) {
        if (result[i] === null) {
          result.splice(i, 1);
          i--;
        }
      }
      // console.log(result);
      if (result.length > 0) {
        return bluebirdPromise.resolve(true);
      }
      return bluebirdPromise.resolve(false);
    });
};

module.exports = new taskService();
