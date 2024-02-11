import documentService from "@/api/document.service";
import moment from 'moment';
import {
    FETCH_DOCUMENTS,
    FETCH_DOCUMENT,
    ADD_DOCUMENT,
    EDIT_DOCUMENT,
    FETCH_DOCUMENT_STRUCTURE,
    EDIT_DOCUMENT_STRUCTURE,
    ADD_DOCUMENT_STRUCTURE,
    GET_STATUS,
    PUSH_DB_CHANGES,
    //mutations
    SET_DOCUMENT,
    SET_DOCUMENTS,
    SET_DOCUMENT_STRUCTURE,
    SET_DOCUMENT_STATUS,
    SET_MESSAGE
} from "./document.constants";

const state = {
    docs: [],
    doc: {},
    docStructure: [],
    docStatus: {}
};

const getters = {
    getDocuments: state => state.docs,
    getDocument: state => state.doc,
    getDocumentStatus: state => state.docStatus,
    getDocumentStructure: state => state.docStructure
};

const actions = {
  [FETCH_DOCUMENTS]: ({commit, rootState}) => {
    console.log("action called")
    return new Promise((resolve, reject) => {
      documentService.getAllDocuments(/*rootState.auth.token*/)
        .then(
          res => {
            console.log("on response module " + res)
            commit(SET_DOCUMENTS, res);
            resolve(res)
          },
          err => {
            commit(SET_MESSAGE, err.message)
            reject(err)
          });
    })
  },
  [FETCH_DOCUMENT]: ({commit, rootState}, payload) => {
      return new Promise((resolve, reject) => {
          documentService.getOneDocument(/*rootState.auth.token,*/payload)
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
  [ADD_DOCUMENT]: ({ commit, rootState }, payload) => {
    console.log("action called")
      return new Promise((resolve, reject) => {
          documentService.addDocument(/*rootState.auth.token,*/ payload)
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
  [EDIT_DOCUMENT]: ({ commit, rootState }, payload) => {
      return new Promise((resolve, reject) => {
          documentService.editDocument(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document updated with success`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err)
              reject(err)
            });
      });
  },
  [FETCH_DOCUMENT_STRUCTURE]: ({commit, rootState}, payload) => {
    return new Promise((resolve, reject) => {
        documentService.getDocumentStructure(/*rootState.auth.token,*/payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_DOCUMENT_STRUCTURE, res);
              resolve(res)
            },
            err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
    })
  },
  [ADD_DOCUMENT_STRUCTURE]: ({ commit, rootState }, payload) => {
    console.log("action called")
      return new Promise((resolve, reject) => {
          documentService.addDocumentStructure(/*rootState.auth.token,*/ payload)
          .then(
            res => {
              commit(SET_MESSAGE, `Document Structure added with success!`);
              resolve(res)
            }, err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
      });
  },
  [EDIT_DOCUMENT_STRUCTURE]: ({ commit, rootState }, payload) => {
    return new Promise((resolve, reject) => {
        documentService.editDocumentStructure(/*rootState.auth.token,*/ payload)
        .then(
          res => {
            commit(SET_MESSAGE, `Document Structure updated with success`);
            resolve(res)
          }, err => {
            commit(SET_MESSAGE, err)
            reject(err)
          });
    });
  },
  [GET_STATUS]: ({commit, rootState}, payload) => {
    return new Promise((resolve, reject) => {
        documentService.getDocumentStatus(/*rootState.auth.token,*/payload)
          .then(
            res => {
              console.log("on response module " + res)
              commit(SET_DOCUMENT_STATUS, res);
              resolve(res)
            },
            err => {
              commit(SET_MESSAGE, err.message)
              reject(err)
            });
    })
  },
  [PUSH_DB_CHANGES]: ({ commit, rootState }, payload) => {
    return new Promise((resolve, reject) => {
        documentService.pushDocumentUpdateToDb(/*rootState.auth.token,*/ payload)
        .then(
          res => {
            commit(SET_MESSAGE, `Document updated with success`);
            resolve(res)
          }, err => {
            commit(SET_MESSAGE, err)
            reject(err)
          });
    });
},
};

export const mutations = {
    [SET_DOCUMENTS]: (state, docs) => {
      state.docs = docs;
    },
    [SET_DOCUMENT]: (state, doc) => {
      state.doc = doc;
    },
    [SET_MESSAGE]: (state, message) => {
      state.message = message;
    },
    [SET_DOCUMENT_STRUCTURE]: (state, docStructure) => {
      state.docStructure = docStructure;
    },
    [SET_DOCUMENT_STATUS]: (state, docStatus) => {
      state.docStatus = docStatus;
    },
  };
  
  export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
  }