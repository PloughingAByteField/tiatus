import { NightwatchAPI, NightwatchTests } from 'nightwatch';

const home: NightwatchTests = {
  'Results Landing Page': () => {
    browser.url('http://127.0.0.1:8080/results/').assert.titleEquals('Tiatus Timing System Test');
  }
};

export default home;