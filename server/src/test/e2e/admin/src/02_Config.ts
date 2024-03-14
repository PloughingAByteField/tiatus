import { NightwatchTests } from 'nightwatch';
import path from 'path';

const races: NightwatchTests = {

  'Config': () => {
    browser
    .useXpath()
    .click("//a[text()='Config']")
    .pause(1000)
    .assert.textContains('//config', 'Current title:')
  },

  'Change Title': () => {
    browser
    .useCss()
    .assert.titleEquals('Tiatus Timing System')
    .setValue('input[id=newTitle]', 'Tiatus Timing System Test')
    .click('button[id=uploadNewTitle]')
    .pause(5000)
    .assert.titleEquals('Tiatus Timing System Test')
  },

  'Change Logo': () => {
    browser
    .useCss()
    .assert.attributeContains('#logo', 'src',  '/results/assets/img/stopwatch.svg')
    .setValue('input[id=newLogo]', path.resolve(__dirname + '/../t1.png'))
    .click('button[id=uploadNewLogo]')
    .pause(5000)
    .assert.attributeContains('#logo', 'src',  '/tiatus/t1.png')
  },

  'Change Footer': () => {
    browser
    .useCss()
    .assert.textEquals('footer', 'Tiatus')
    .setValue('input[id=newFooter]', 'The Footer')
    .click('button[id=uploadNewFooter]')
    .pause(5000)
    .assert.textEquals('footer', 'Tiatus / The Footer')
  }
};

export default races;