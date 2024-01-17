import docAppService from "@/api/documentApplication.service";
import moment from 'moment';
import {
    FETCH_DOCUMENT_APPLICATION,
    UPSERT_RABBIT_MQ,
    //mutations
    SET_DOCUMENT_APPLICATION,
    SET_MESSAGE
} from "./document-application.constants";

const state = {
    docApp: {},
};

const getters = {
    getDocumentApplication: state => state.docApp,
};

const actions = {
  [FETCH_DOCUMENT_APPLICATION]: ({commit, rootState}, payload) => {
      return new Promise((resolve, reject) => {
        console.log("getOneDocumentApplication");
        docAppService.getOneDocumentApplication(/*rootState.auth.token,*/payload)
            .then(
              res => {
                console.log("on response module " + res)
                commit(SET_DOCUMENT, res);
                resolve(res)
              },
              err => {
                commit(SET_MESSAGE, err.message)
                reject(err)
              });
      })
  },
  [UPSERT_RABBIT_MQ]: ({ commit, rootState }, payload) => {
    console.log("action called")
      return new Promise((resolve, reject) => {
        docAppService.upsertRabbitMq(/*rootState.auth.token,*/ payload)
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
    [SET_DOCUMENT_APPLICATION]: (state, docApp) => {
      state.doc = docApp;
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