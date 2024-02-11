import { 
  // Mutations
  SET_MESSAGE 
} from "./constants";

const state = {
  message:"",
};

const getters = {};

const actions = {};

export const mutations = {
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