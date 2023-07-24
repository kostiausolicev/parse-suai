import json


class VectorList:
    def __init__(self):
        self.vectorList: list = None

    def get_vectors_list(self):
        return self.vectorList

    @staticmethod
    def get_vectors():
        vector_path = "../Python/vectors/all_vectors_information.json"
        res = VectorList()
        res.vectorList = []
        with open(vector_path, encoding="utf-8") as file:
            data = json.load(file)
            for i in list(data.keys()):
                res.vectorList.append(i + ' ' + data.get(i).get("name"))
        return res

    @staticmethod
    def get_ability_lists(vtr: str):
        vector_path = "../Python/vectors/all_vectors_information.json"
        res = VectorList()
        res.vectorList = []
        with open(vector_path, encoding="utf-8") as file:
            data = json.load(file)
            for l in data.get(vtr).keys():
                if data.get(vtr).get(l) == '-':
                    continue
                match l:
                    case "linkBudget": res.vectorList.append("Бюджет")
                    case "linkContract": res.vectorList.append("Контракт")
                    case "linkSpecial": res.vectorList.append("Особая квота")
                    case "linkSeparate": res.vectorList.append("Отдельная квота")
                    case "linkContractAbroad": res.vectorList.append("Контракт иностранцы")
        return res
