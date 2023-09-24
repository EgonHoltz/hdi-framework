import applicationService from "@/api/application.service";
import moment from 'moment';
import {
    FETCH_APPLICATIONS,
    ADD_APPLICATION,
    EDIT_APPLICATION,
    //mutations
    SET_APPLICATION,
    SET_APPLICATIONS,
    SET_MESSAGE
} from "./application.constants";

const state = {
    apps: [],
    app: {}
};

const getters = {
    getApplications: state => state.apps,
    getApplication: state => state.app
};

const actions = {
    [FETCH_APPLICATIONS]: ({commit, rootState}) => {
        return new Promise((resolve, reject) => {
            applicationService.getAllApplications(/*rootState.auth.token*/)
              .then(
                res => {
                  console.log("on response module " + res)
                  commit(SET_APPLICATIONS, res);
                  resolve(res)
                },
                err => {
                  commit(SET_MESSAGE, err.message)
                  reject(err)
                });
        })
    },
    [ADD_APPLICATION]: ({ commit, rootState }, payload) => {
        return new Promise((resolve, reject) => {
            applicationService.addApplication(/*rootState.auth.token,*/ payload)
            .then(
              res => {
                commit(SET_MESSAGE, `Application added with success!`);
                resolve(res)
              }, err => {
                commit(SET_MESSAGE, err.message)
                reject(err)
              });
        });
    },
    [EDIT_APPLICATION]: ({ commit, rootState }, payload) => {
        return new Promise((resolve, reject) => {
            applicationService.editApplication(/*rootState.auth.token,*/ payload)
            .then(
              res => {
                commit(SET_MESSAGE, `Application updated with success`);
                resolve(res)
              }, err => {
                commit(SET_MESSAGE, err)
                reject(err)
              });
        });
      },
};

export const mutations = {
    [SET_APPLICATIONS]: (state, apps) => {
      state.apps = apps;
    },
    [SET_APPLICATION]: (state, app) => {
      state.app = app;
    },
    [SET_MESSAGE]: (state, message) => {
      state.message = message;
    }
  };
  
  export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
  }