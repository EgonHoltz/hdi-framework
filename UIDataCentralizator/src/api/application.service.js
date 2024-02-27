import API_URL from './config.js'

export const appService = {
    // TODO: add token as receiving data on method
    async getAllApplications(/*token*/) {
        let response = await fetch(`${API_URL}/application/`, {
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
    async addApplication(/*token,*/ payload) {
      const response = await fetch(`${API_URL}/application/`, {
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
    async editApplication(/*token,*/ payload) {
      const response = await fetch(`${API_URL}/application/${payload.id}`, {
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
  
  export default appService;