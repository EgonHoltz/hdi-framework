import documentDataService from "@/api/document-data.service";
import moment from 'moment';
import {
    FETCH_DOCUMENT_DATA,
    FETCH_PENDING_DELETE_DATA,
    SOFT_DELETE_DATA,
    FINISH_DELETE_DATA,
    //mutations
    SET_DOCUMENT_DATA,
    SET_PENDING_DELETE_DATA,
    SET_MESSAGE
} from "./document-data.constants";

const state = {
    docData: [],
    docPendingData: [],
};

const getters = {
    getDocumentData: state => state.docData,
    getPendingDeleteData: state => state.docPendingData,
};

const actions = {
  [FETCH_DOCUMENT_DATA]: ({commit, rootState}, payload) => {
    console.log("action FETCH_DOCUMENT_DATA called")
    return new Promise((resolve, reject) => {
      documentDataService.getDocumentDataByFilters(/*rootState.auth.token,*/payload)
        .then(
          res => {
            console.log("on response module " + res)
            commit(SET_DOCUMENT_DATA, res);
            resolve(res)
          },
          err => {
            commit(SET_MESSAGE, err.message)
            reject(err)
          });
    })
  },
  [FETCH_PENDING_DELETE_DATA]: ({commit, rootState}, payload) => {
    console.log("action FETCH_PENDING_DELETE_DATA called")
    return new Promise((resolve, reject) => {
      documentDataService.getPendingDeleteData(/*rootState.auth.token,*/payload)
        .then(
          res => {
            console.log("on response module " + res)
            commit(SET_PENDING_DELETE_DATA, res);
            resolve(res)
          },
          err => {
            commit(SET_MESSAGE, err.message)
            reject(err)
          });
    })
  },
  [SOFT_DELETE_DATA]: ({ commit, rootState }, payload) => {
    console.log("action SOFT_DELETE_DATA")
      return new Promise((resolve, reject) => {
        documentDataService.addSoftDeleteData(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [FINISH_DELETE_DATA]: ({ commit, rootState }, payload) => {
    console.log("action FINISH_DELETE_DATA")
      return new Promise((resolve, reject) => {
        documentDataService.finishDeleteData(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
};

export const mutations = {
    [SET_DOCUMENT_DATA]: (state, docData) => {
      state.docData = docData;
    },
    [SET_PENDING_DELETE_DATA]: (state, docPendingData) => {
      state.docPendingData = docPendingData;
    },
    [SET_MESSAGE]: (state, message) => {
      state.message = message;
    },
  };
  
  export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
  }