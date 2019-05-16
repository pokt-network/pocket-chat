const AionWeb3 = require('aion-web3');
const NodesmithURL = 'https://aion.api.nodesmith.io/v1/mastery/jsonrpc?apiKey=' + process.env.NODESMITH_API_KEY;
const PocketChatContractBuild = require('../build/bolts/PocketChat.json');
const DeployerAccountPK = process.env.DEFAULT_AION_PK;

if (!DeployerAccountPK) {
    throw new Error('Invalid private key, set your private key in the DEFAULT_AION_PK env variable');
}

function getWeb3Instance() {
    const web3 = new AionWeb3(new AionWeb3.providers.HttpProvider(NodesmithURL));
    var account = web3.eth.accounts.privateKeyToAccount(DeployerAccountPK);
    web3.eth.defaultAccount = account.address;
    web3.eth.accounts.wallet.add(account);
    return web3;
}

async function deployContract() {
    const web3 = getWeb3Instance();
    const PocketChat = new web3.eth.Contract(PocketChatContractBuild.abi, {
        from: web3.eth.defaultAccount,
        data: PocketChatContractBuild.bytecode
    });

    if (!PocketChat) {
        throw new Error('Error parsing the PocketChat contract build file');
    }

    const pocketChatInstanceGasEstimate = await PocketChat.deploy({
        arguments: [web3.eth.defaultAccount]
    }).estimateGas({
        from: web3.eth.defaultAccount,
        gas: 5000000
    });

    console.log("Gas estimate: " + pocketChatInstanceGasEstimate);

    const pocketChatInstance = await PocketChat.deploy({
        arguments: [web3.eth.defaultAccount]
    }).send({
        from: web3.eth.defaultAccount,
        gas: 5000000
    });

    if (!pocketChatInstance) {
        throw new Error('An error ocurred during deployment');
    }

    console.log(pocketChatInstance.options.address);
}

deployContract();
