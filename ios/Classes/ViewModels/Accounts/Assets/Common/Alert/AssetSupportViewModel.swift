// Copyright 2019 Algorand, Inc.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//    http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

//
//  AssetSupportViewModel.swift

import UIKit

class AssetSupportViewModel {
    private(set) var title: String?
    private(set) var id: String?
    private(set) var detail: String?
    private(set) var assetDisplayViewModel: AssetDisplayViewModel?

    init(draft: AssetAlertDraft) {
        setTitle(from: draft)
        setId(from: draft)
        setDetail(from: draft)
        setAssetDisplayViewModel(from: draft)
    }

    private func setTitle(from draft: AssetAlertDraft) {
        title = draft.title
    }

    private func setId(from draft: AssetAlertDraft) {
        id = "\(draft.assetIndex)"
    }

    private func setDetail(from draft: AssetAlertDraft) {
        detail = draft.detail
    }

    private func setAssetDisplayViewModel(from draft: AssetAlertDraft) {
        assetDisplayViewModel = AssetDisplayViewModel(assetDetail: draft.assetDetail)
    }
}
