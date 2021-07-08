const seatContainer = document.querySelector('.seat-container');
const proceedBtn = document.querySelector('.proceed-btn');
const priceLabel = document.querySelector('.price');
const sessionsSelect = document.querySelector('.sessions');
const sessionsLabel = document.querySelector('.session-label');
const buyInfo = document.querySelector('.bay-menu');
const seatToBuyArray = [];
const oneSeatPrice = 11.99;
const url = 'http://localhost:8080/cinema/'
let purchased = [];
let buyBtn;
let sum;

sessionsSelect.addEventListener('change', (e) => {
    addSeatsToContainer(120, e.target.value);
});

proceedBtn.addEventListener('click', () => {
    seatContainer.classList.toggle('is-invisible');
    priceLabel.classList.toggle('is-invisible');
    buyInfo.classList.toggle('z-index-negative');
    buyInfo.classList.toggle('z-index-positive');

    proceedBtn.innerHTML = buyInfo.classList.contains('z-index-positive')
        ? 'Back to seat choose'
        :  'Proceed';
});

seatContainer.addEventListener('click', (e) => {
    if (!e.target.classList.contains('purchased')) {
        if (e.target.classList.contains('checked-box') || e.target.classList.contains('box')) {
            e.target.classList.toggle('checked-box');
            e.target.classList.toggle('box');
            addOrRemoveSeat(e);
            if (seatToBuyArray.length > 0) {
                proceedBtn.classList.add('is-visible');
                proceedBtn.classList.remove('is-invisible');

                sessionsSelect.style.visibility = 'hidden';
                sessionsLabel.style.visibility = 'hidden';
                sessionsSelect.style.display = 'none';
                sessionsLabel.style.display = 'none';

            } else {
                proceedBtn.classList.remove('is-visible');
                proceedBtn.classList.add('is-invisible');

                sessionsSelect.style.visibility = 'visible';
                sessionsLabel.style.visibility = 'visible';
                sessionsSelect.style.display = 'block';
                sessionsLabel.style.display = 'block';
            }

            showPriceAndTicketInfo();
        }
    }

});

function showPriceAndTicketInfo() {
    sum = 0;
    if (seatToBuyArray.length > 0) {
        seatToBuyArray.forEach((e) => sum += Number.parseFloat(e.dataset.price));

    }
    sum = sum.toFixed(2);

    priceLabel.innerHTML = sum > 0
        ? `You have chosen ${seatToBuyArray.length} tickets, ` + 'Price: ' + sum
        : '';
    buyInfo.innerHTML = '';
    let h1Title = document.createElement('h1');
    h1Title.innerHTML = sum > 0
        ? `You have chosen ${seatToBuyArray.length} tickets.`
        : '';
    h1Title.classList.add('info-child');
    buyInfo.append(h1Title);
    seatToBuyArray.forEach((e) => {
        let session = e.dataset.session;
        let part1 = session.slice(0, 2);
        let part2 = session.slice(2, 4);
        session = part1 + ':' + part2;
        buyInfo.innerHTML += `<span class="info-child" style='display:inline'>Row : ${e.dataset.row} Place : ${e.dataset.column} at ${session}</span>`;
    });
    let textPrice = document.createElement('h1');
    textPrice.classList.add('info-child');
    textPrice.innerHTML = `Price: ${sum}`;
    buyInfo.append(textPrice);
    buyBtn = document.createElement('button');
    buyBtn.classList.add('proceed-btn');
    buyBtn.innerHTML = 'Buy';
    buyBtn.addEventListener('click', buyAction);
    buyInfo.append(buyBtn);
}

function addSeatsToContainer(quantity, session) {
    const filteredArray = purchased.filter((e) =>  {
        console.log(e.sessionId + ' ' + session);
        return e.sessionId.toString() === session;
    });
    seatContainer.innerHTML = '';

    for (let i = 0; i < quantity; i++) {
        let row = (i / 10 + 1) << 0;
        let column = i % 10 + 1;
        let purchasedB = false;
        purchased.forEach((s) => {
            if (s.sessionId == session && s.row == row && s.columnn == column) {
                purchasedB = true;
                console.log(row + '' + column);
                seatContainer.innerHTML +=
                    `<div class="box purchased" data-row="${row}" data-column="${column}" data-price="${oneSeatPrice}" data-session="${sessionsSelect.value}">${row}-${column}</div>`;
            }
        });
        if (!purchasedB) {
            seatContainer.innerHTML +=
                `<div class="box" data-row="${row}" data-column="${column}" data-price="${oneSeatPrice}" data-session="${sessionsSelect.value}">${row}-${column}</div>`;
        }
    }
}

function addOrRemoveSeat(e) {
    if (e.target.classList.contains('box')) {
        const index = seatToBuyArray.indexOf(e.target);
        if (index > -1) {
            seatToBuyArray.splice(index, 1);
        }

    } else if (e.target.classList.contains('checked-box')) {
        seatToBuyArray.push(e.target);
    }
}

function buyAction() {
    const postJson = constructData();
    console.log(postJson);
    buyPostAction(postJson);
    location.reload();
}

function constructData() {
    const data = [];
    seatToBuyArray.forEach((e) => {
        data.push({
            session: e.dataset.session,
            row: e.dataset.row,
            column: e.dataset.column,
            });
    });
    return data;
}

buyPostAction = async (data) => {
    try {
        const config = {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        }
        const response = await fetch(url + 'hallServlet', config);
        if (response.ok) {
            // return await response.json();
            console.log('ok')
        } else {
            if (response.status >= 400) {
                throw new Error("Bad response from server");
            }
        }
    } catch (error) {
        console.log(error);
    }
}

allPurchasedTickets = async () => {
    try {
        const response = await fetch(url + 'hallServlet');
        if (response.ok) {
            purchased = [];
            const seats = await response.json();
            purchased.push(...seats);
            addSeatsToContainer(120, 1800);
            console.log(purchased);
        } else {
            throw new Error("Bad response from server");
        }
    } catch (e) {
        console.log(e);
    }
}

allPurchasedTickets();

proceedBtn.classList.add('is-invisible');

window.setInterval(() => {
    allPurchasedTickets();
    sessionsSelect.value = '1800';
}, 60000);
