import json
import os

from responses.utils.Utils import Utils
from responses.vectors_inform.Applicant import Applicant
from responses.vectors_inform.MinimalPoints import MinimalPoints


class VectorData:
    def __init__(self):
        self.link_budget = None
        self.link_contract = None
        self.link_contract_abroad = None
        self.link_separate = None
        self.link_special = None
        self.budgetPlaces = None
        self.contractPlaces = None
        self.exams = None
        self.minimalPointsBudget = None
        self.minimalPointsContract = None
        self.vector = None

    def get_link_contract(self):
        return self.link_contract

    def set_link_contract(self, value):
        self.link_contract = value
        return self

    def get_link_contract_abroad(self):
        return self.link_contract_abroad

    def set_link_contract_abroad(self, value):
        self.link_contract_abroad = value
        return self

    def get_link_separate(self):
        return self.link_separate

    def set_link_separate(self, value):
        self.link_separate = value
        return self

    def get_link_special(self):
        return self.link_special

    def set_link_special(self, value):
        self.link_special = value
        return self

    def set_vector(self, vector: list):
        self.vector = vector
        return self

    def get_vector(self):
        return self.vector

    def get_link_budget(self) -> str:
        return self.link_budget

    def set_link_budget(self, link_to_lists: str) -> 'VectorData':
        self.link_budget = link_to_lists
        return self

    def get_budget_places(self) -> int:
        return self.budgetPlaces

    def set_budget_places(self, budget_places: int) -> 'VectorData':
        self.budgetPlaces = budget_places
        return self

    def get_contract_places(self) -> int:
        return self.contractPlaces

    def set_contract_places(self, contract_places: int) -> 'VectorData':
        self.contractPlaces = contract_places
        return self

    def get_exams(self) -> list:
        return self.exams

    def set_exams(self, exams: list) -> 'VectorData':
        self.exams = exams
        return self

    def get_minimal_points_budget(self) -> MinimalPoints:
        return self.minimalPointsBudget

    def set_minimal_points_budget(self, minimal_points_budget: MinimalPoints) -> 'VectorData':
        self.minimalPointsBudget = minimal_points_budget
        return self

    def get_minimal_points_contract(self) -> MinimalPoints:
        return self.minimalPointsContract

    def set_minimal_points_contract(self, minimal_points_contract: MinimalPoints) -> 'VectorData':
        self.minimalPointsContract = minimal_points_contract
        return self

    def to_dict(self) -> dict:
        d = vars(self)
        d["vector"] = [i.to_dict() for i in self.vector] if self.vector is not None else None
        return d

    @staticmethod
    def parse_json(vector_name: str, t: str) -> 'VectorData':
        vector_path = "../Python/vectors/all_vectors_information.json"
        with open(vector_path, "r") as file:
            data = json.load(file)
        result = VectorData()
        try:
            atr = data[vector_name]
            result.set_exams(atr["exams"]).\
                set_minimal_points_budget(atr["minimalPointsBudget"]).\
                set_minimal_points_contract(atr["minimalPointsContract"]).\
                set_budget_places(atr["budgetPlaces"]).\
                set_contract_places(atr["contractPlaces"]).\
                set_link_budget(atr["link_budget"]). \
                set_link_special(atr["link_special"]). \
                set_link_separate(atr["link_separate"]).\
                set_link_contract(atr["link_contract"]).\
                set_link_contract_abroad(atr["link_contract_abroad"])
        except KeyError:
            return result
        vector_path = "../Python/vectors/" + vector_name + "&" + t + ".json"
        if not os.path.exists(vector_path):
            match t:
                case "budget": url = result.get_link_budget()
                case "contract": url = result.get_link_contract()
                case "special": url = result.get_link_special()
                case "separate": url = result.get_link_separate()
                case "contract_abroad": url = result.get_link_contract_abroad()
                case _: return VectorData()
            Utils.parse_to_json(vector_path, url)
        with open(vector_path, encoding="utf-8") as file1:
            with open("../Python/vectors/all_vectors_information.json", encoding="utf-8") as file2:
                d = json.load(file2)
                match t:
                    case "budget":
                        url = d[vector_name]["link_budget"]
                    case "contract":
                        url = d[vector_name]["link_contract"]
                    case "special":
                        url = d[vector_name]["link_special"]
                    case "separate":
                        url = d[vector_name]["link_separate"]
                    case "contract_abroad":
                        url = d[vector_name]["link_contract_abroad"]
                    case _:
                        return VectorData()
            data = json.load(file1)
            Utils.update_json(data["update"], vector_path, url)
        abit_list = []
        for count, item in enumerate(data["list"]):
            abit = Applicant()
            try:
                abit_list.append(abit.set_snils(item['snils']).
                                 set_priority(item["priority"]).
                                 set_all_points(item["allPoints"]).
                                 set_exams_points(item["examsPoints"]).
                                 set_bvi(item['bvi']).
                                 set_additional_points(item['additionalPoints']).
                                 set_points(item['points']).
                                 set_names_profile(item['namesProfile']).
                                 set_original_documents(item['originalDocuments']).
                                 set_consent(item['consent']))
            except KeyError:
                continue
        return result.set_vector(abit_list)
