import API_URL from './config.js'

export const docAppService = {
  // TODO: add token as receiving data on method
  async getOneDocumentApplication(/*token,*/ payload) {
    console.log("calling getOneDocumentApplication " + payload);
    let response = await fetch(`${API_URL}/document/${payload.documentId}/application/${payload.applicationId}`, {
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
  async getMqRabbitFromConfiguration(/*token,*/ payload) {
    console.log("calling getMqRabbitFromConfiguration " + payload);
    let response = await fetch(`${API_URL}/document/${payload.documentId}/application/${payload.applicationId}/mqqueue`, {
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
  async upsertRabbitMq(/*token,*/ payload) {
    console.log("calling upsertRabbitMq " + payload);
    const id = payload.id;
    delete payload.id;
    console.log("calling creation with doc " +id.documentId + " and app " + id.applicationId);
    const response = await fetch(`${API_URL}/document/${id.documentId}/application/${id.applicationId}/mqqueue`, {
      method: "PUT",
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
  async getSftpFromConfiguration(/*token,*/ payload) {
    console.log("calling getSftpFromConfiguration " + payload);
    let response = await fetch(`${API_URL}/document/${payload.documentId}/application/${payload.applicationId}/sftp`, {
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
  async upsertSftp(/*token,*/ payload) {
    console.log("calling upsertSftp " + payload);
    const id = payload.id;
    delete payload.id;
    console.log("calling creation with doc " +id.documentId + " and app " + id.applicationId);
    const response = await fetch(`${API_URL}/document/${id.documentId}/application/${id.applicationId}/sftp`, {
      method: "PUT",
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
  async getGrpcFromConfiguration(/*token,*/ payload) {
    console.log("calling getGrpcFromConfiguration " + payload);
    let response = await fetch(`${API_URL}/document/${payload.documentId}/application/${payload.applicationId}/grpc`, {
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
  async upsertGrpc(/*token,*/ payload) {
    console.log("calling upsertGrpc " + payload);
    const id = payload.id;
    delete payload.id;
    console.log("calling creation with doc " +id.documentId + " and app " + id.applicationId);
    const response = await fetch(`${API_URL}/document/${id.documentId}/application/${id.applicationId}/grpc`, {
      method: "PUT",
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
  async getDocumentWithSftpByApplication(/*token,*/ payload) {
    console.log("calling getDocumentWithSftpByApplication " + payload);
    let response = await fetch(`${API_URL}/scheduler/sftp/application/${payload.applicationId}`, {
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
  async getSftpSchedulerConfiguration(/*token,*/ payload) {
    let appId = payload.appId;
    let docId = payload.docId;
    console.log("calling getDocumentWithSftpByApplication " + payload);
    let response = await fetch(`${API_URL}/scheduler/application/${appId}/document/${docId}`, {
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
      console.log("Error on service : " + response)
      throw Error(handleResponses(response.status));
    }
  },
  async upsertSftpSchedulerConfiguration(/*token,*/ payload) {
    let appId = payload.appId;
    let docId = payload.docId;
    let sftpSchedulerConfig = {};
    sftpSchedulerConfig.dataMode = payload.dataMode;
    sftpSchedulerConfig.fileType = payload.fileType;
    sftpSchedulerConfig.cron = payload.cron;
    console.log("calling upsertSftpSchedulerConfiguration " + payload);
    let response = await fetch(`${API_URL}/scheduler/application/${appId}/document/${docId}`, {
      method: "PUT",
      headers: {
        'Content-Type': 'application/json',
        //'Authorization': token
      },
      body: JSON.stringify(sftpSchedulerConfig)
    });
    if (response.ok) {
      console.log("Service called: " + response)
      return await response.json();
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
  
  export default docAppService;