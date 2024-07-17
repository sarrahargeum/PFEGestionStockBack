const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8099/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/notifications', (greeting) => {
        showGreeting("test");
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendOrder() {
    const orderData = {
        code: "BEF101",
        dateCommande: "2023-06-25T10:00:00Z",
        etatCommande: "EN_PREPARATION",
        idMagasin: 1,
        fournisseur: {
            id: 1
        },
        ligneEntrees: [
            {
                article: {
                    id: 1
                },
                quantite: 10,
                etatCommande: "EN_PREPARATION",
                prixUnitaire: "11"
            }
        ]
    };

    stompClient.publish({
        destination: "/StockMnager/api/BonEntree/saveBF", // Remplacez ceci par votre destination appropriée
        body: JSON.stringify(orderData)
    });
}

function showGreeting(message) {
    console.log("abc")
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendOrder());
});