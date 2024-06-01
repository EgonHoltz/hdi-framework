import API_URL from './config.js'

export const auditLoggerService = {
  // TODO: add token as receiving data on method
  async getAllFilesAuditLogger(/*token,*/ payload) {
    let appId = payload.appId;
    let docId = payload.docId;
    console.log("calling getAllFilesAuditLogger " + payload);
    let response = await fetch(`${API_URL}/auditLogger/document/${docId}/application/${appId}`, {
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
  
  export default auditLoggerService;