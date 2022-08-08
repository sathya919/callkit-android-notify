import Foundation

@objc public class callkitandroid: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
