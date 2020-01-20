/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




self.addEventListener('activate', async () => {
  // This will be called only once when the service worker is activated.
  try {
      console.log("HOOOOLA");
    const options = {}
    const subscription = await self.registration.pushManager.subscribe(options)
     console.log(JSON.stringify(subscription))
    sendSubscriptionToServer(subscription);
  } catch (err) {
    console.log('Error', err)
  }
})

function sendSubscriptionToServer(subscription) {

    // Get public key and user auth from the subscription object
    var key = subscription.getKey ? subscription.getKey('p256dh') : '';
    var auth = subscription.getKey ? subscription.getKey('auth') : '';

    console.log(key);
    console.log(auth);
    console.log("ADENTRO");
    // This example uses the new fetch API. This is not supported in all
    // browsers yet.
    return fetch('api/subscription/getSubscription', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            endpoint: subscription.endpoint,
            // Take byte[] and turn it into a base64 encoded string suitable for
            // POSTing to a server over HTTP
            key: key ? btoa(String.fromCharCode.apply(null, new Uint8Array(key))) : '',
            auth: auth ? btoa(String.fromCharCode.apply(null, new Uint8Array(auth))) : ''
        })
    });
}

self.addEventListener('push', function(event) {
  if (event.data) {
    console.log('Push event!! ', event.data.text());
    
    showLocalNotification('Que ganas de morirse', event.data.text(), self.registration);
  } else {
    console.log('Push event but no data');
  }
});

const showLocalNotification = (title, body, swRegistration) => {
  const options = {
    body
    // here you can add more properties like icon, image, vibrate, etc.
  }
  swRegistration.showNotification(title, options)
}