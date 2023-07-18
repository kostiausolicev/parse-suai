class MinimalPoints:
    def __init__(self):
        self.oneYearAgo = None
        self.twoYearAgo = None
        self.treeYearAgo = None

    def get_one_years_later(self) -> int:
        return self.oneYearAgo

    def set_one_years_later(self, one_years_later: int) -> 'MinimalPoints':
        self.oneYearAgo = one_years_later
        return self

    def get_two_years_later(self) -> int:
        return self.twoYearAgo

    def set_two_years_later(self, two_years_later: int) -> 'MinimalPoints':
        self.twoYearAgo = two_years_later
        return self

    def get_tree_years_later(self) -> int:
        return self.treeYearAgo

    def set_tree_years_later(self, tree_years_later: int) -> 'MinimalPoints':
        self.treeYearAgo = tree_years_later
        return self

    def to_dict(self) -> dict:
        d = vars(self)
        return d
