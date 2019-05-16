let fs = require('fs');
const {
    expect
} = require('chai');
let {
    compile,
    web3
} = require('./common.js');
let pocketChatSol = fs.readFileSync(`${process.cwd()}` + '/contracts/PocketChat.sol', {
    encoding: 'utf8'
});

// Constants
let MAX_GAS = 2000000;

// Test state
let pocketChatABI;
let pocketChatByteCode;
let pocketChatAddress;
let pocketChatInstance;

describe('Pocket Chat', () => {

    it('should compile contract', async () => {
        let {
            PocketChat
        } = await compile(web3, pocketChatSol)
        pocketChatABI = PocketChat.info.abiDefinition
        pocketChatByteCode = PocketChat.code
        expect(pocketChatABI).to.not.be.null
        expect(pocketChatABI).to.be.an('array')
        expect(pocketChatByteCode).to.not.be.null
        expect(pocketChatByteCode).to.be.an('string')
    }).timeout(0)

    it('should deploy the contract', async () => {
        var PocketChatContract = new web3.eth.Contract(pocketChatABI, {
            from: web3.eth.defaultAccount,
            data: pocketChatByteCode
        });
        var nrg = await PocketChatContract.deploy({
            arguments: [web3.eth.defaultAccount],
            data: pocketChatByteCode
        }).estimateGas({
            from: web3.eth.defaultAccount
        });
        console.log("CONTRACT STORAGE NRG ESTIMATE: " + nrg);
        pocketChatInstance = await PocketChatContract.deploy({
            arguments: [web3.eth.defaultAccount],
            data: pocketChatByteCode
        }).send({
            from: web3.eth.defaultAccount,
            gas: MAX_GAS
        });
        pocketChatAddress = pocketChatInstance.options.address;
        expect(pocketChatInstance).to.not.be.null;
        expect(pocketChatAddress).to.not.be.null;
    }).timeout(0);

    describe('#sendMessage', function() {
        it('should send a message', async function(){
            const txReceipt = await pocketChatInstance.methods.sendMessage(web3.eth.defaultAccount, 'Test message')
                .send({
                    from: web3.eth.defaultAccount,
                    gas: MAX_GAS
                });
            expect(txReceipt).to.be.ok;
            expect(txReceipt.status).to.be.true;
        }).timeout(0);
    });

    describe('#getMessageByIndex', function() {
        it('should retrieve a message with index 0', async function(){
            const messageArr = await pocketChatInstance.methods.getMessageByIndex(0)
                .call({
                    from: web3.eth.defaultAccount
                });
            expect(messageArr['0']).to.equal(web3.eth.defaultAccount);
            expect(messageArr['1']).to.equal('Test message');
        }).timeout(0);
    });

    describe('#getTotalMessageCount', function() {
        it('should retrieve the total message count', async function() {
            const count = await pocketChatInstance.methods.getTotalMessageCount()
                .call({
                    from: web3.eth.defaultAccount
                });
            expect(count).to.be.equal('1');
        }).timeout(0);
    })
});

