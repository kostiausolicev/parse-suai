import json


class VectorList:
    def __init__(self):
        self.vectorList: list = VectorList.get_vectors()

    def get_vectors_list(self):
        return self.vectorList

    @staticmethod
    def get_vectors():
        vector_path = "../Python/vectors/all_vectors_information.json"
        with open(vector_path) as file:
            data = json.load(file)
            keys = list(data.keys())
        return keys
