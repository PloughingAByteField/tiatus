import { NightwatchAPI, NightwatchTests } from 'nightwatch';

const home: NightwatchTests = {
  'Results Landing Page': () => {
    browser.url('http://127.0.0.1:4200/results/').assert.titleEquals('Tribesmen RC Head of the River 2024');
  },

  'Race 1 Nav Link': () => {
    browser
      .waitForElementVisible('body', 1000)
      .assert.visible('a.nav-link')
      .assert.textContains('sidebar nav ul li:first-child a', 'Race 1')
      .click('sidebar nav ul li:first-child a')
      .pause(1000)
      .assert.visible('main race-results race-results-table')
      .assert.textContains('race-results-table', 'Results for Race 1 from Start to Finish')
      .assert.titleEquals('Tribesmen RC Head of the River 2024 - Race 1')
  },

  'Race 2 Nav Link': () => {
    browser
      .click('sidebar nav ul li:nth-child(2) a')
      .pause(1000)
      .assert.visible('main race-results race-results-table')
      .assert.textContains('race-results-table', 'Results for Race 2 from Start to Finish')
      .assert.titleEquals('Tribesmen RC Head of the River 2024 - Race 2')
  },

  'Race 3 Nav Link': () => {
    browser
      .click('sidebar nav ul li:last-child a')
      .pause(1000)
      .assert.visible('main race-results race-results-table')
      .assert.textContains('race-results-table', 'Results for Race 3 from Start to Finish')
      .assert.titleEquals('Tribesmen RC Head of the River 2024 - Race 3')
  }
};

export default home;