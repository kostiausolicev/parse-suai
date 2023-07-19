import json
import os

from responses.vectors_inform.Applicant import Applicant
from responses.vectors_inform.MinimalPoints import MinimalPoints


class VectorData:
    def __init__(self):
        self.link = None
        self.budgetPlaces = None
        self.contractPlaces = None
        self.exams = None
        self.minimalPointsBudget = None
        self.minimalPointsContract = None
        self.vector = None

    def set_vector(self, vector: list):
        self.vector = vector
        return self

    def get_vector(self):
        return self.vector

    def get_link_to_lists(self) -> str:
        return self.link

    def set_link_to_lists(self, link_to_lists: str) -> 'VectorData':
        self.link = link_to_lists
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
    def parse_json(vector_name: str) -> 'VectorData':
        vector_path = "../Python/vectors/all_vectors_information.json"
        with open(vector_path, "r") as file:
            data = json.load(file)
        result = VectorData()
        atr = data[vector_name]
        result.set_exams(atr["exams"]).\
            set_minimal_points_budget(atr["minimalPointsBudget"]).\
            set_minimal_points_contract(atr["minimalPointsContract"]).\
            set_budget_places(atr["budgetPlaces"]).\
            set_contract_places(atr["contractPlaces"]).\
            set_link_to_lists(atr["link"])
        vector_path = "../Python/vectors/" + vector_name + ".json"
        if not os.path.exists(vector_path):
            return VectorData()
        with open(vector_path, encoding="utf-8") as file1:
            data = json.load(file1)
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
