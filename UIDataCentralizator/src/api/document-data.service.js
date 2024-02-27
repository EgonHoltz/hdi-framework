import API_URL from './config.js'

export const docService = {
  // TODO: add token as receiving data on method
  async getDocumentDataByFilters(/*token,*/ payload) {
    let documentId = payload.docId;
    let page = payload.currentPage;
    let size = payload.pageSize;
    console.log("calling creation");
    const response = await fetch(`${API_URL}/document/db/${documentId}?page=${page}&size=${size}`, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      },
      body: JSON.stringify(payload.filters)
    })
    if (response.ok) {
      try {
        const json = await response.json();
        return json;
      } catch (error){
        throw new Error("Invalid JSON response from the server");
      }
    } else {
      throw Error(handleResponses(response.status));
    }
  },
  async getPendingDeleteData(/*token,*/ payload) {
    console.log("calling getPendingDeleteData");
    let response = await fetch(`${API_URL}/document/db/${payload}/softdelete`, {
      method: "GET",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      }
    });
    if (response.ok) {
      console.log("Service called: " + response)
      return await response.json();
    } else {
      throw Error(handleResponses(response.status));
    }
  },
  async addSoftDeleteData(/*token,*/ payload) {
    const documentId = payload.documentId;
    const dataId = payload.dataId;
    const response = await fetch(`${API_URL}/document/db/${documentId}/softdelete/${dataId}`, {
      method: "DELETE",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      }
    });
    if (response.ok) {
      return await response;
    } else {
      throw Error(handleResponses(response.status));
    }
  },
  async finishDeleteData(/*token,*/ payload) {
    const documentId = payload.documentId;
    const dataId = payload.dataId;
    const isDelete = payload.isDelete;
    const response = await fetch(`${API_URL}/document/db/${documentId}/harddelete/${dataId}?isDelete=${isDelete}`, {
      method: "DELETE",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      }
    });
    if (response.ok) {
      return await response;
    } else {
      throw Error(handleResponses(response.status));
    }
  },
};

function handleResponses(code) {
    let message = ""
    switch (code) {
      case 401:
        message = "You are not allowed to proceed with this operation!"
        break;
      case 400:
        message = "Invalid data was informed, please review"
        break;
      default:
        message = "Unknow error, ask for IT support"
        break;
    }
    return message
  }
  
  export default docService;