import { NightwatchAPI, NightwatchTests } from 'nightwatch';

const home: NightwatchTests = {
  'Admin Login Page': () => {
    browser.url('http://127.0.0.1:8080/').assert.titleEquals('Please sign in');
  },

  'Login': () => {
    browser
      .waitForElementVisible('body', 1000)
      .setValue('input[name=username]', 'admin')
      .setValue('input[name=password]', '12345678')
      .click('button')
      .pause(1000)
      .assert.titleEquals('Tiatus Timing System')
  }
};

export default home;