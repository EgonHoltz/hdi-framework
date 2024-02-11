import API_URL from './config.js'

export const docService = {
  // TODO: add token as receiving data on method
  async getAllDocuments(/*token*/) {
      let response = await fetch(`${API_URL}/document/`, {
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
  async getOneDocument(/*token,*/ payload) {
    console.log("calling getOne");
    let response = await fetch(`${API_URL}/document/${payload}`, {
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
  async addDocument(/*token,*/ payload) {
    console.log("calling creation");
    const response = await fetch(`${API_URL}/document/`, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      },
      body: JSON.stringify(payload)
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
  async editDocument(/*token,*/ payload) {
    const response = await fetch(`${API_URL}/document/${payload.id}`, {
      method: "PUT",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      },
      body: JSON.stringify(payload)
    });
    if (response.ok) {
      return await response.json();
    } else {
      throw Error(handleResponses(response.status));
    }
  }, 
  async getDocumentStructure(/*token,*/ payload) {
    console.log("calling getOne");
    let response = await fetch(`${API_URL}/document/structure/${payload}`, {
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
  async addDocumentStructure(/*token,*/ payload) {
    console.log("calling creation");
    const id = payload.id;
    delete payload.id;
    const response = await fetch(`${API_URL}/document/structure/${id}`, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      },
      body: JSON.stringify(payload)
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
  async editDocumentStructure(/*token,*/ payload) {
    const id = payload.id;
    delete payload.id;
    const response = await fetch(`${API_URL}/document/structure/${id}`, {
      method: "PUT",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      },
      body: JSON.stringify(payload)
    });
    if (response.ok) {
      return await response.json();
    } else {
      throw Error(handleResponses(response.status));
    }
  },
  async getDocumentStatus (/*token,*/ payload) {
    const id = payload;
    const response = await fetch(`${API_URL}/document/${id}/dbStatus`, {
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
  async pushDocumentUpdateToDb(/*token,*/ payload) {
    const id = payload.id;
    delete payload.id;
    const response = await fetch(`${API_URL}/document/${id}/dbStatus`, {
      method: "PUT",
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