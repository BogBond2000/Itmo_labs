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
                stderr=subprocess.PIPE,
                text=True  # Автоматическая обработка строк (utf-8)
        ) as process:
            stdout, stderr = process.communicate(input=key)

            return stdout.strip(), stderr.strip()


    def test_1(self):
        stdout, stderr = self.run_tests('third word')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_2(self):
        stdout, stderr = self.run_tests('first')
        self.assertEqual(stdout, 'the first one')
        self.assertEqual(stderr,'')

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
        stdout, stderr = self.run_tests('sec_ond')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_7(self):
        stdout, stderr = self.run_tests('second')
        self.assertEqual(stdout, 'the second one')
        self.assertEqual(stderr,'')

    def test_8(self):
        stdout, stderr = self.run_tests('sec')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_9(self):
        stdout, stderr = self.run_tests('first')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

    def test_10(self):
        stdout, stderr = self.run_tests('two word')
        self.assertEqual(stdout, '')
        self.assertEqual(stderr,'Phrase can not in list')

if __name__ == '__main__':
        unittest.main()
