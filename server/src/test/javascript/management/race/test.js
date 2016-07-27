describe("src.test.javascript.management.race.test.js", function() {
    it("dummy test", function() {
        expect(true).toBe(true);
    });
    it("call js", function() {
        var y = test();
        expect(y).toBe(1);
    });
});