import docAppService from "@/api/documentApplication.service";
import moment from 'moment';
import {
    FETCH_DOCUMENT_APPLICATION,
    FETCH_RABBIT_MQ,
    UPSERT_RABBIT_MQ,
    FETCH_SFTP,
    UPSERT_SFTP,
    //mutations
    SET_DOCUMENT_APPLICATION,
    SET_MQ_RABBIT,
    SET_SFTP,
    SET_MESSAGE
} from "./document-application.constants";

const state = {
    docApp: {},
    mqRabbit: [],
    sftp: []
};

const getters = {
    getDocumentApplication: state => state.docApp,
    getRabbitMq: state => state.mqRabbit,
    getSftp: state => state.sftp
};

const actions = {
  [FETCH_DOCUMENT_APPLICATION]: ({commit, rootState}, payload) => {
      return new Promise((resolve, reject) => {
        console.log("getOneDocumentApplication");
        docAppService.getOneDocumentApplication(/*rootState.auth.token,*/payload)
            .then(
              res => {
                console.log("on response module " + res)
                commit(SET_DOCUMENT_APPLICATION, res);
                resolve(res)
              },
              err => {
                commit(SET_MESSAGE, err.message)
                reject(err)
              });
      })
  },
  [FETCH_RABBIT_MQ]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_RABBIT_MQ")
      return new Promise((resolve, reject) => {
        docAppService.getMqRabbitFromConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_MQ_RABBIT, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
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
  [FETCH_SFTP]: ({ commit, rootState }, payload) => {
    console.log("action FETCH_SFTP")
      return new Promise((resolve, reject) => {
        docAppService.getSftpFromConfiguration(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_SFTP, res);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [UPSERT_SFTP]: ({ commit, rootState }, payload) => {
    console.log("action UPSERT_SFTP")
      return new Promise((resolve, reject) => {
        docAppService.upsertSftp(/*rootState.auth.token,*/ payload)
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
      state.docApp = docApp;
    },
    [SET_MQ_RABBIT]: (state, mqRabbit) => {
      state.mqRabbit = mqRabbit;
    },
    [SET_SFTP]: (state, sftp) => {
      state.sftp = sftp;
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