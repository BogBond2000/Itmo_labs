import subprocess
import unittest

class TestDictionaryProgram(unittest.TestCase):

    def run_tests(self, key):
        if not key.endswith('\n'):
            key += '\n'
        with subprocess.Popen(
                ['./main'],
                stdin=subprocess.PIPE,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE
        ) as process:
            stdout, stderr = process.communicate(input=key.encode())


            return (stdout).decode().strip(), stderr.decode().strip()

    def test_1(self):
        stdout, stderr = self.run_tests('third word')
        self.assertEqual(stdout, 'third word explanation')
        self.assertEqual(stderr,'')

    def test_2(self):
        stdout, stderr = self.run_tests('third worssd')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_3(self):
        stdout, stderr = self.run_tests('third ')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_4(self):
        stdout, stderr = self.run_tests('second word')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_5(self):
        stdout, stderr = self.run_tests('second wor')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_6(self):
        stdout, stderr = self.run_tests('ten word')
        self.assertEqual(stdout, 'ten word explanation')
        self.assertEqual(stderr,'')

    def test_7(self):
        stdout, stderr = self.run_tests('fife word')
        self.assertEqual(stdout, 'fife word explanation')
        self.assertEqual(stderr,'')

    def test_8(self):
        stdout, stderr = self.run_tests('two   word')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_9(self):
        stdout, stderr = self.run_tests('one word')
        self.assertEqual(stdout, 'one word explanation')
        self.assertEqual(stderr,'')

    def test_10(self):
        stdout, stderr = self.run_tests('two word')
        self.assertEqual(stdout, 'two word explanation')
        self.assertEqual(stderr,'')

if __name__ == '__main__':
        unittest.main()
