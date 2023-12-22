import Vue from "vue";
import Vuex from "vuex";
import moduleBase from "./module";
import moduleApplication from "./application/application.module";
import moduleDocument from "./document/document.module"

Vue.use(Vuex);

export default new Vuex.Store({
    getters: moduleBase.getters,
    mutations: moduleBase.mutations,
    state: moduleBase.state,
    actions: moduleBase.actions,
    modules: {
      application: moduleApplication,
      document: moduleDocument,
    },
  });