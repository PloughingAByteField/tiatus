import { NightwatchTests } from 'nightwatch';

const login: NightwatchTests = {
  'Login': () => {
    browser
      .url('http://127.0.0.1:8080/')
      .waitForElementVisible('body', 1000)
      .assert.titleEquals('Please sign in')
      .setValue('input[name=username]', 'admin')
      .setValue('input[name=password]', '12345678')
      .click('button')
      .pause(1000)
      .assert.titleEquals('Tiatus Timing System')
  }

};

export default login;