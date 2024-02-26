import documentDataService from "@/api/document-data.service";
import moment from 'moment';
import {
    FETCH_DOCUMENT_DATA,
    //mutations
    SET_DOCUMENT_DATA,
    SET_MESSAGE
} from "./document-data.constants";

const state = {
    docData: [],
};

const getters = {
    getDocumentData: state => state.docData,
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
};

export const mutations = {
    [SET_DOCUMENT_DATA]: (state, docData) => {
      state.docData = docData;
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