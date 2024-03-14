import { NightwatchTests } from 'nightwatch';

const races: NightwatchTests = {

  'Races': () => {
    browser
    .useXpath()
    .click("//a[text()='Race']")
    .pause(1000)
    .assert.textContains('//races', 'Add Race')
  },

  'Add Race 1': () => {
    browser
    .useCss()
    .setValue('input[id=newName]', 'Race 1')
    .setValue('input[id=newRaceOrder]', '1')
    .click('button')  
    .pause(5000)
    .assert.elementsCount('races table tr', 1)
  },

  'Add Race 2': () => {
    browser
    .useCss()
    .setValue('input[id=newName]', 'Race 2')
    .setValue('input[id=newRaceOrder]', '2')
    .click('button')  
    .pause(1000)
    .assert.elementsCount('races table tr', 2)
  },

  'Add Race 3': () => {
    browser
    .useCss()
    .setValue('input[id=newName]', 'Race 3')
    .setValue('input[id=newRaceOrder]', '3')
    .click('button')  
    .pause(1000)
    .assert.elementsCount('races table tr', 3)
  }

};

export default races;